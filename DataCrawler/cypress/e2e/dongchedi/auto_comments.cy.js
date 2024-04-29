const carId = 311
const startPageNumber = 700
describe("车友圈内容", () => {


    beforeEach(() => {
        cy.visit(`https://www.dongchedi.com/community/${carId}/${startPageNumber}`)
    })

    it("get all cards", () => {
        cy.crawlCommunityCard(carId,startPageNumber)
        cy.get('li[class*="pagination-item"]').parent().then(ul => {
            const totalPagesIndex = Cypress.$(ul).find('li').length - 1
            const totalPages = Cypress.$(ul).find(`li:nth-child(${totalPagesIndex})`).find('a span').html()
            for (let pageNumber = startPageNumber + 1; pageNumber <= totalPages; pageNumber++) {
                cy.visit(`https://www.dongchedi.com/community/${carId}/${pageNumber}`)
                cy.crawlCommunityCard(carId,pageNumber)
            }

        })

    })
})