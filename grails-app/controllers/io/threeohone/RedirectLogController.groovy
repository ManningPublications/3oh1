package io.threeohone

import grails.plugin.springsecurity.annotation.Secured
import pl.touk.excel.export.WebXlsxExporter
import pl.touk.excel.export.XlsxExporter

@Secured(['isAuthenticated()'])
class RedirectLogController {


    def index() {

        throw new Exception();

        List<RedirectLog> logs = RedirectLog.list()

        def headers = [
                message(code: 'redirectLog.dateCreated.label'),
                message(code: 'shortener.shortenerKey.label'),
                message(code: 'shortener.destinationUrl.label'),
                message(code: 'redirectLog.clientIp.label'),
                message(code: 'redirectLog.referer.label'),
                message(code: 'redirectLog.userAgent.label')
        ]

        def withProperties = ['dateCreated', 'shortener.shortenerKey', 'shortener.destinationUrl', 'clientIp', 'referer', 'userAgent']


        def filename = "Redirect Logs - ${new Date().format('yyyy-MM-dd_hh-mm-ss')}"
        def worksheetName = "All Redirects"

        createXlsxResponse(filename, worksheetName, headers, withProperties, logs)

    }

    def show() {

        def shortener = Shortener.get(params.shortenerId)


        if (!shortener) {
            response.status = 404
            return
        }

        List<RedirectLog> logs = RedirectLog.where { shortener == shortener }.list()


        def headers = [
                message(code: 'redirectLog.dateCreated.label'),
                message(code: 'redirectLog.clientIp.label'),
                message(code: 'redirectLog.referer.label'),
                message(code: 'redirectLog.userAgent.label')
        ]

        def withProperties = ['dateCreated', 'clientIp', 'referer', 'userAgent']

        def filename = "Redirect Logs ${shortener.shortenerKey} - ${new Date().format('yyyy-MM-dd_hh-mm-ss')}"
        def worksheetName = "Redirects for ${shortener.shortenerKey}"

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