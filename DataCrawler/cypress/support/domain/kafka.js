export class KafkaSender {


    toJson() {
        throw new Error("toJson method is not implemented")
    }

    kafkaTopic() {
        throw new Error("kafkaTopic method is not implemented")
    }

    sendToKafka() {

        fetch(`http://localhost:8888/kafka/send/${this.kafkaTopic()}`, {
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