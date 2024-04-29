describe("车型信息列表", () => {


    beforeEach(() => {
        cy.visit("https://www.dongchedi.com/auto/library/x-x-x-x-x-x-x-x-x-x-x")
    })

    it("click brands one by one then get car information", () => {
        cy.get('li[class*="brand_letter"]').each($brand_letter => {
            $brand_letter.each((letter_index, letter) => {
                const letter_val = Cypress.$(letter).html()
                if (letter_val.length === 1) {
                    console.log(`.............${letter_val}.................`)
                    cy.get('li[class*="brand_letter"]').contains(letter_val).click()
                    retrieveAllBrands()
                }

            })
        })


    })
})


function retrieveAllBrands() {

    cy.get('a[class*="brand_link"]').then($brandLink => {
        $brandLink.each((index, link) => {
            const href = link.getAttribute("href")
            const brandName = Cypress.$(link).children('span[class*="brand_name"]').first().html()

            cy.get(`a[href="${href}"]`).click()
            cy.wait(3000)
            cy.log(`send ${brandName} data to kafka`)

            cy.get('span[class^="sort_count"]').invoke('html').then(carCount => {
                if (!carCount || parseInt(carCount) === 0) {
                    console.log(`${brandName} has no cars`);
                } else {
                    console.log(`${brandName} has ${carCount} cars`)
                    cy.crawlCarDetail(brandName)
                }


            })

        })


    })
}