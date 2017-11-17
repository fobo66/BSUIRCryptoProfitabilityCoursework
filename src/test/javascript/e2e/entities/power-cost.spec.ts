import {browser, element, by, $} from 'protractor';
import {NavBarPage} from './../page-objects/jhi-page-objects';

const path = require('path');

describe('PowerCost e2e test', () => {

    let navBarPage: NavBarPage;
    let powerCostDialogPage: PowerCostDialogPage;
    let powerCostComponentsPage: PowerCostComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);


    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load PowerCosts', () => {
        navBarPage.goToEntity('power-cost');
        powerCostComponentsPage = new PowerCostComponentsPage();
        expect(powerCostComponentsPage.getTitle()).toMatch(/courseworkApp.powerCost.home.title/);

    });

    it('should load create PowerCost dialog', () => {
        powerCostComponentsPage.clickOnCreateButton();
        powerCostDialogPage = new PowerCostDialogPage();
        expect(powerCostDialogPage.getModalTitle()).toMatch(/courseworkApp.powerCost.home.createOrEditLabel/);
        powerCostDialogPage.close();
    });

    it('should create and save PowerCosts', () => {
        powerCostComponentsPage.clickOnCreateButton();
        powerCostDialogPage.setCityInput('city');
        expect(powerCostDialogPage.getCityInput()).toMatch('city');
        powerCostDialogPage.setPricePerKilowattInput('5');
        expect(powerCostDialogPage.getPricePerKilowattInput()).toMatch('5');
        powerCostDialogPage.save();
        expect(powerCostDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class PowerCostComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-power-cost div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class PowerCostDialogPage {
    modalTitle = element(by.css('h4#myPowerCostLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    cityInput = element(by.css('input#field_city'));
    pricePerKilowattInput = element(by.css('input#field_pricePerKilowatt'));
    setCityInput = function (city) {
        this.cityInput.sendKeys(city);
    }
    getCityInput = function () {
        return this.cityInput.getAttribute('value');
    }
    setPricePerKilowattInput = function (pricePerKilowatt) {
        this.pricePerKilowattInput.sendKeys(pricePerKilowatt);
    }
    getPricePerKilowattInput = function () {
        return this.pricePerKilowattInput.getAttribute('value');
    }

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
