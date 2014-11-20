package models

import com.dslplatform.api.client.{Bootstrap, ClientPersistableRepository, CrudProxy, JsonSerialization}
import com.dslplatform.api.patterns.PersistableRepository
import models.WorldWonders.Wonder

import scala.concurrent.Future

trait WonderCrud {

  implicit val locator = Bootstrap.init(getClass.getResourceAsStream("/dsl-project.props"))
  val jsonSerialization = locator.resolve[JsonSerialization]

  private lazy val wonderRepository = new ClientPersistableRepository[Wonder](locator) // what should be here: locator.resolve[PersistableRepository[Wonder]]
  private lazy val crudProxy = locator.resolve[CrudProxy]

  /** Create a new wonder */
  def createC(wonder: Wonder): Future[String] =
    wonderRepository.insert(wonder)

  /** Create new wonders */
  def createC(wonders: Seq[Wonder]): Future[IndexedSeq[String]] =
    wonderRepository.insert(wonders)

  /** Find existing wonder by name (URI) */
  def readC(uri: String): Future[Wonder] =
    wonderRepository.find(uri)

  /** Find all existing wonders */
  def readAllC: Future[IndexedSeq[Wonder]] = {
    wonderRepository.search
  }

  /** Update an existing wonder */
  def updateC(wonder: Wonder): Future[Unit] =
    wonderRepository.update(wonder)

  /** Update a list of existing wonders */
  def updateC(wonders: Seq[Wonder]): Future[Unit] =
    wonderRepository.update(wonders)

  /** Delete a wonder */
  def deleteC(wonderURI: String): Future[Unit] =
    crudProxy.delete(wonderURI) //wonderRepository.delete(wonder)

  /** Delete a list of wonders */
  def deleteC(wondersURIs: Seq[String]): Future[Unit] = {
    wonderRepository.find(wondersURIs).flatMap(wonderRepository.delete)
  }

  /** Delete all wonders */
  def deleteAllC(): Future[Unit] = {
    wonderRepository.search().flatMap(wonderRepository.delete)
  }
}
