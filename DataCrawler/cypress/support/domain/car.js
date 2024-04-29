import {KafkaSender} from './kafka'

export class CommunityCard extends KafkaSender {

    constructor(carId, username, userHref, userTitle, userCarRegistration, cardHref, header, cardContent,pageNumber) {
        super();
        this.carId = carId;
        this.username = username;
        this.userHref = userHref;
        this.userTitle = userTitle;
        this.userCarRegistration = userCarRegistration;
        this.cardHref = cardHref;
        this.header = header;
        this.cardContent = cardContent;
        this.pageNumber = pageNumber;
    }


    toJson() {
        return {
            "carId": this.carId,
            "username": this.username,
            "userHref": this.userHref,
            "userTitle": this.userTitle,
            "userCarRegistration": this.userCarRegistration,
            "cardHref": this.cardHref,
            "header": this.header,
            "cardContent": this.cardContent,
            "pageNumber": this.pageNumber,

        }

    }

    kafkaTopic() {
        return 'car_community_card'
    }

}




export class CarDetail extends KafkaSender {
    constructor(brand, tag, name, href, score) {
        super();
        this.brand = brand
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
            "brand": this.brand, "name": this.name, "tag": this.tag, "id": this.id, "score": this.score,
        }
    }


    kafkaTopic() {
        return 'car_detail'
    }
}