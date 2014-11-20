package controllers

import models._
import WorldWonders.Wonder
import dispatch.{url, Http, as}

import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

object Rest extends Controller
    with WonderCrud
    with CustomParsers
    with CustomWriteables {

  private lazy val wonderParser = parseObject[Wonder]
  private lazy val wonderArrayParser = parseIndexedSeq[Wonder]

  case class A(batchcomplete: String, query: B)

  case class B(pages: List[P])

  case class P(pageid: Int, ns: Int, title: String, imagerepository: String, imageinfo: Array[I])

  case class I(thumburl: String, thumbwidth: Int, thumbheight: Int, url: String, descriptionurl: String)

  case class Result(title: String, url: String)

  def getRandFromWiki: Future[Result] = {
    val svc = url(
      "http://commons.wikimedia.org/w/api.php?continue=&format=json&action=query&generator=random&prop=imageinfo&iiprop=url&iiurlwidth=200")
    Http(svc > as.Bytes).map {
      wikiResponse =>
        val a = jsonSerialization.deserialize[A](wikiResponse)
        a.query.pages.headOption.flatMap {
          case page =>
            if (page.imageinfo.isEmpty) None
            else {
              page.imageinfo.headOption.map {
                ii =>
                  Result(page.title.drop(4).take(5) + " [REPLACE ME]", ii.thumburl)
              }
            }
        }.get
    }.recoverWith {
      case _ => getRandFromWiki
    }
  }

  def randomimg() = Action.async {
    getRandFromWiki.map(r => Ok(jsonSerialization.serialize(r)))
  }

  def reset() = Action.async {
    replaceAllC().map(r => Ok(r))
  }

  def read(URI: String) = Action.async {
    readC(URI) map { wonder =>
      Ok(wonder)
    }
  }

  def readAll = Action.async {
    readAllC map { wonders =>
      Ok(wonders)
    }
  }

  def create() = Action.async(wonderParser) { implicit request =>
    createC(request.body).map(Ok(_))
  }

  def replace(URI: String) = Action.async(wonderParser) { implicit request =>
    updateC(request.body).map(Ok(_))
  }

  def delete(URI: String) = Action.async { implicit request =>
    deleteC(URI).map { _ => Ok(s"$URI successfully deleted")}
  }

  def deleteAll() = Action.async {
    deleteAllC().map { _ => Ok("All deleted!")}
  }
}
