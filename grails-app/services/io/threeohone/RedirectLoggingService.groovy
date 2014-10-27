package io.threeohone

class RedirectLoggingService {

    def userAgentIdentService


    def log(Map params) {

        new RedirectLog(
                shortener: params.shortener,
                clientIp: params.clientIp,
                referer: params.referer,
                clientInformation: parseClientInformationFromRequest()
        ).save(failOnError: true)
    }

    private ClientInformation parseClientInformationFromRequest() {

        new ClientInformation(
                browserName: userAgentIdentService.getBrowser(),
                browserVersion: userAgentIdentService.getBrowserVersion(),
                operatingSystem: userAgentIdentService.getOperatingSystem(),
                mobileBrowser: userAgentIdentService.isMobile()
        )
    }
}
