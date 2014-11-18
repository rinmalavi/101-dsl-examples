package controllers

import models._
import WorldWonders.Wonder

import play.api.mvc.{ Action, Controller }

object Rest extends Controller
    with WonderCrud
    with CustomParsers
    with CustomWriteables {

  private lazy val wonderParser      = parseObject[Wonder]
  private lazy val wonderArrayParser = parseIndexedSeq[Wonder]

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

  def create() = Action.async(wonderParser){ implicit request =>
    createC(request.body).map(Ok(_))
  }

  def replace(URI: String) = Action.async(wonderParser){ implicit request =>
    updateC(request.body).map(Ok(_))
  }

  def delete(URI: String) = Action.async{ implicit  request =>
    deleteC(URI).map{_ => Ok(s"$URI successfully deleted")}
  }

  def deleteAll() = Action.async{
    deleteAllC().map{ _ => Ok("All deleted!")}
  }
}
