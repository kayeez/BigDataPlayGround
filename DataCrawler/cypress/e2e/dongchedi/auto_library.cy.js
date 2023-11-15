
class CarDetail {
    constructor(tag, name, href, score) {
        this.tag = tag || "";
        this.name = name || "";
        this.href = href || "";
        this.id = this.extractCarId(href)
        this.score = score || "";
    }

    extractCarId(hrefStr) {
        if (!hrefStr) {
            return "";
        }
        return hrefStr.substring(hrefStr.lastIndexOf("/") + 1);
    }

    toJson() {
        return {
            "name": this.name, "tag": this.tag, "id": this.id, "score": this.score,
        }
    }

     sendToKafka() {
         fetch('http://localhost:8888/kafka/send/car_detail', {
             method: 'POST',
             headers: {
                 'Content-Type': 'application/json'
             },
             body: JSON.stringify(this.toJson())
         })
             .then(response => {
                 if (!response.ok) {
                     throw new Error(`HTTP error! Status: ${response.status}`);
                 }
                 return response.json();
             })
             .then(responseData => {
                 console.log('Response Data:', responseData);
             })
             .catch(error => {
                 console.error('Error:', error.message);
             });
    }

}


describe("车型信息列表", () => {


    beforeEach(() => {
        cy.visit("https://www.dongchedi.com/auto/library/x-x-x-x-x-x-x-x-x-x-x")
    })

    it("click brands one by one then get car information", () => {
        cy.get('li[class*="brand_letter"]').contains("B").click()

        cy.get('a[class*="brand_link"]').then($brandLink => {
            let brandMap = {}
            $brandLink.each((index, link) => {
                const href = link.getAttribute("href")
                const brandName = Cypress.$(link).children('span[class*="brand_name"]').first().html()
                brandMap[brandName] = href
            })
            for (let name in brandMap) {
                const brandHref = brandMap[name]
                cy.get(`a[href="${brandHref}"]`).click().then(() => {
                    cy.get('li[class*="car-list_item"]', {timeout: 30000}).then($carListItems => {
                        $carListItems.each((_, $carListItem) => {
                            const carListItem = Cypress.$($carListItem)
                            const carTag = carListItem.find('span[class*="series-card_tag"]').html()
                            const carName = carListItem.find('a[class*="series-card_name"]').html()
                            const carHref = carListItem.find('a[class*="series-card_name"]').attr("href")
                            const carScore = carListItem.find('a[class*="series-card_score"]').text()
                            const car = new CarDetail(carTag, carName, carHref, carScore)
                            console.log(car)
                            car.sendToKafka()

                        })


                    })

                })
            }
        })


    })
})