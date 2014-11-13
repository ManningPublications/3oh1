package io.threeohone

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['permitAll'])
class ClientLocationController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]




    def location() {

        def code = ""
        withLocation { location ->

            if (location) {

                code = location.countryCode


                new ClientLocation(
                        countryCode: location.countryCode,
                        countryName: location.countryName,
                        region: location.region,
                        city: location.city,
                        postalCode: location.postalCode,
                        latitude: location.latitude,
                        longitude: location.longitude,
                        dmaCode: location.dma_code,
                        areaCode: location.area_code,
                        metroCode: location.metro_code
                ).save()

            }


        }

        render text: "hallo aus ${code}"
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ClientLocation.list(params), model: [clientLocationInstanceCount: ClientLocation.count()]
    }

    def show(ClientLocation clientLocationInstance) {
        respond clientLocationInstance
    }

    def create() {
        respond new ClientLocation(params)
    }

    @Transactional
    def save(ClientLocation clientLocationInstance) {
        if (clientLocationInstance == null) {
            notFound()
            return
        }

        if (clientLocationInstance.hasErrors()) {
            respond clientLocationInstance.errors, view: 'create'
            return
        }

        clientLocationInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'clientLocation.label', default: 'ClientLocation'), clientLocationInstance.id])
                redirect clientLocationInstance
            }
            '*' { respond clientLocationInstance, [status: CREATED] }
        }
    }

    def edit(ClientLocation clientLocationInstance) {
        respond clientLocationInstance
    }

    @Transactional
    def update(ClientLocation clientLocationInstance) {
        if (clientLocationInstance == null) {
            notFound()
            return
        }

        if (clientLocationInstance.hasErrors()) {
            respond clientLocationInstance.errors, view: 'edit'
            return
        }

        clientLocationInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'ClientLocation.label', default: 'ClientLocation'), clientLocationInstance.id])
                redirect clientLocationInstance
            }
            '*' { respond clientLocationInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(ClientLocation clientLocationInstance) {

        if (clientLocationInstance == null) {
            notFound()
            return
        }

        clientLocationInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'ClientLocation.label', default: 'ClientLocation'), clientLocationInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'clientLocation.label', default: 'ClientLocation'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
