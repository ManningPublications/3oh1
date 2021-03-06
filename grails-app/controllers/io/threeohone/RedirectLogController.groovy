package io.threeohone

import grails.plugin.springsecurity.annotation.Secured
import pl.touk.excel.export.WebXlsxExporter
import pl.touk.excel.export.XlsxExporter

@Secured(['isAuthenticated()'])
class RedirectLogController {


    def index() {

        List<RedirectLog> logs = RedirectLog.list()

        def headers = [
                message(code: 'redirectLog.dateCreated.label'),
                message(code: 'shortener.key.label'),
                message(code: 'shortener.destinationUrl.label'),
                message(code: 'redirectLog.referer.label'),
                message(code: 'clientInformation.operatingSystem.label'),
                message(code: 'clientInformation.browserName.label'),
                message(code: 'clientInformation.browserVersion.label'),
                message(code: 'clientLocation.countryCode.label'),
                message(code: 'clientLocation.countryName.label'),
                message(code: 'clientLocation.city.label')
        ]
        def withProperties = [
                'dateCreated',
                'shortener.key', 'shortener.destinationUrl',
                'referer',
                'clientInformation.operatingSystem',
                'clientInformation.browserName',
                'clientInformation.browserVersion',
                'clientLocation.countryCode',
                'clientLocation.countryName',
                'clientLocation.city'
        ]



        def filename = "Redirect Logs - ${new Date().format('yyyy-MM-dd_hh-mm-ss')}"
        def worksheetName = "All Redirects"

        createXlsxResponse(filename, worksheetName, headers, withProperties, logs)

    }

    def show() {


        def shortener = Shortener.findByKey(params.shortenerId)


        if (!shortener) {
            response.status = 404
            return
        }

        List<RedirectLog> logs = RedirectLog.where { shortener == shortener }.list()


        def headers = [
                message(code: 'redirectLog.dateCreated.label'),
                message(code: 'redirectLog.referer.label'),
                message(code: 'clientInformation.operatingSystem.label'),
                message(code: 'clientInformation.browserName.label'),
                message(code: 'clientInformation.browserVersion.label'),
                message(code: 'clientLocation.countryCode.label'),
                message(code: 'clientLocation.countryName.label'),
                message(code: 'clientLocation.city.label')
        ]

        def withProperties = [
                'dateCreated',
                'referer',
                'clientInformation.operatingSystem',
                'clientInformation.browserName',
                'clientInformation.browserVersion',
                'clientLocation.countryCode',
                'clientLocation.countryName',
                'clientLocation.city'
        ]

        def filename = "Redirect Logs ${shortener.key} - ${new Date().format('yyyy-MM-dd_hh-mm-ss')}"
        def worksheetName = "Redirects for ${shortener.key}"

        createXlsxResponse(filename, worksheetName, headers, withProperties, logs)

    }

    private void createXlsxResponse(String filename, String worksheetName, List<String> headers, List<String> properties, List<RedirectLog> logs) {

        def webXlsxExporter = new WebXlsxExporter()
        webXlsxExporter.setWorksheetName(worksheetName)

        webXlsxExporter.with {
            setResponseHeaders(response, filename + XlsxExporter.filenameSuffix)
            fillHeader(headers)
            add(logs, properties)
            save(response.outputStream)
        }
    }
}
