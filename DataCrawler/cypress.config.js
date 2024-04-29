const {defineConfig} = require("cypress");

module.exports = defineConfig({
    chromeWebSecurity: false,
    numTestsKeptInMemory: 0,
    defaultCommandTimeout: 10000,
    e2e: {
        setupNodeEvents(on, config) {
            // implement node event listeners here
        },
    },
});
