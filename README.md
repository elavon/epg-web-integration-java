# Elavon Payment Gateway Integration Demo - Using Java Spring Boot

This is an example merchant site using java and Spring Boot integrating with the Elavon Payment Gateway

## Table of Contents

- [Background](#background)
- [Security](#security)
- [Documentation](#documentation)
- [Prerequisites](#prerequisites)
- [Build](#build)
- [Usage](#usage)
- [License](#license)

## Background
An Elavon Payment Gateway Hosted Payments Page (HPP) is a web page containing a payment form that is hosted by the Elavon Payment Gateway. Since the page isn't hosted on your server, no PCI sensitive data needs to touch your server.

There are two forms of HPP Integration.

**Lightbox**
With a lightbox display, EPG opens a modal window 'over the top' of your website with a payment form inside the modal. Your shopper can enter payment information and checkout while remaining within 'viewing distance' of your website. We recommend Lightbox for e-commerce and other website integrations as opening a payment form on top of your existing site gives shoppers a more unified look and feel between their shopping and checkout experiences on your site.

**Redirect**
With a redirect display, a shopper is 'moved' to a checkout page that is entirely separate from the page they were originally on.  It routes a customer to an external page hosted by Elavon where they can input payment data.

This sample shows a simple web site making all the necessary backend API calls and javascript calls to demonstrate both integration techniques.  More information on the HPP integration can be found at https://developer.elavon.com/products/elavon-payment-gateway/v1/hosted-payments-overview. 


## Security
Default credentials are included which will allow you to create transactions in our uat environment.

If you want to view transactions in our portal https://uat.converge.eu.elavonaws.com/login you need to obtain a test account.

To get a test account contact product support
- **EU:**
    - **Email:** epgsupporteurope@elavon.com
    - **Phone(s):**
        - **United Kingdom:** 0345 604 2329 (Monday to Friday, 9:00 to 17:00 GMT)
        - **Ireland:** 0818 924 177 (Monday to Friday, 9:00 to 17:00 GMT)
        - **Poland:** (+48) 22 3065894 (Monday to Friday, 10:00-18:00 CET/CEST)

- **NA:**
    - **Email:** techsupp@elavon.com
    - **Phone (USA):** +1 800 377 3962 (24/7)

If you have received test credentials you can replace our default credentials in application.yml.  Update merchantAlias, secretKey, and publicApiKey.


## Prerequisites
Java 17
<br /> Maven

## Build
The project uses lombok which has a plugin for intellij and eclipse

build \
mvn clean install

## Usage

run \
`java -jar target/web.integration-0.0.1-SNAPSHOT.jar`

An example for running if behind a proxy server \
`java -Dhttps.proxyHost=127.0.0.1 -Dhttps.proxyPort=3128 -Dhttp.proxyHost=127.0.0.1 -Dhttp.proxyPort=3128 -Dhttp.nonProxyHosts="127.*|localhost" -jar target/web.integration-0.0.1-SNAPSHOT.jar`

Open your browser url \
http://localhost:8080

Pick an integration method.

Use a test cards which can be found at https://developer.elavon.com/products/elavon-payment-gateway/v1/testing.



## Documentation
You can find the EPG documentation [on the website](https://developer.elavon.com/products/elavon-payment-gateway/v1/overview).

For the HPP Redirect integration you must create an order and a paymentsession.  In this example those calls can be found in EPGApiClient.java.
Our API reference documentation can be found at
* [API Reference](https://developer.elavon.com/products/elavon-payment-gateway/v1/api-reference)



## License

- This project is licensed under the terms of the Apache 2.0 open source license. Please refer to [LICENSE](./LICENSE) for the full terms.



