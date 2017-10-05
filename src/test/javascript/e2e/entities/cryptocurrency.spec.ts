import {browser, element, by, $} from 'protractor';
import {NavBarPage} from './../page-objects/jhi-page-objects';

const path = require('path');

describe('Cryptocurrency e2e test', () => {

    let navBarPage: NavBarPage;
    let cryptocurrencyDialogPage: CryptocurrencyDialogPage;
    let cryptocurrencyComponentsPage: CryptocurrencyComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);


    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Cryptocurrencies', () => {
        navBarPage.goToEntity('cryptocurrency');
        cryptocurrencyComponentsPage = new CryptocurrencyComponentsPage();
        expect(cryptocurrencyComponentsPage.getTitle()).toMatch(/courseworkApp.cryptocurrency.home.title/);

    });

    it('should load create Cryptocurrency dialog', () => {
        cryptocurrencyComponentsPage.clickOnCreateButton();
        cryptocurrencyDialogPage = new CryptocurrencyDialogPage();
        expect(cryptocurrencyDialogPage.getModalTitle()).toMatch(/courseworkApp.cryptocurrency.home.createOrEditLabel/);
        cryptocurrencyDialogPage.close();
    });

    it('should create and save Cryptocurrencies', () => {
        cryptocurrencyComponentsPage.clickOnCreateButton();
        cryptocurrencyDialogPage.setNameInput('name');
        expect(cryptocurrencyDialogPage.getNameInput()).toMatch('name');
        cryptocurrencyDialogPage.setShortNameInput('shortName');
        expect(cryptocurrencyDialogPage.getShortNameInput()).toMatch('shortName');
        cryptocurrencyDialogPage.setPriceInput('5');
        expect(cryptocurrencyDialogPage.getPriceInput()).toMatch('5');
        cryptocurrencyDialogPage.save();
        expect(cryptocurrencyDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CryptocurrencyComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-cryptocurrency div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CryptocurrencyDialogPage {
    modalTitle = element(by.css('h4#myCryptocurrencyLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    shortNameInput = element(by.css('input#field_shortName'));
    priceInput = element(by.css('input#field_price'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function (name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function () {
        return this.nameInput.getAttribute('value');
    }

    setShortNameInput = function (shortName) {
        this.shortNameInput.sendKeys(shortName);
    }

    getShortNameInput = function () {
        return this.shortNameInput.getAttribute('value');
    }

    setPriceInput = function (price) {
        this.priceInput.sendKeys(price);
    }

    getPriceInput = function () {
        return this.priceInput.getAttribute('value');
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
