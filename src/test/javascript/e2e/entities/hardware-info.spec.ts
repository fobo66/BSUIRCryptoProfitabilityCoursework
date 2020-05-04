import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('HardwareInfo e2e test', () => {

    let navBarPage: NavBarPage;
    let hardwareInfoDialogPage: HardwareInfoDialogPage;
    let hardwareInfoComponentsPage: HardwareInfoComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load HardwareInfos', () => {
        navBarPage.goToEntity('hardware-info');
        hardwareInfoComponentsPage = new HardwareInfoComponentsPage();
        expect(hardwareInfoComponentsPage.getTitle()).toMatch(/courseworkApp.hardwareInfo.home.title/);

    });

    it('should load create HardwareInfo dialog', () => {
        hardwareInfoComponentsPage.clickOnCreateButton();
        hardwareInfoDialogPage = new HardwareInfoDialogPage();
        expect(hardwareInfoDialogPage.getModalTitle()).toMatch(/courseworkApp.hardwareInfo.home.createOrEditLabel/);
        hardwareInfoDialogPage.close();
    });

   /* it('should create and save HardwareInfos', () => {
        hardwareInfoComponentsPage.clickOnCreateButton();
        hardwareInfoDialogPage.setHashPowerInput('5');
        expect(hardwareInfoDialogPage.getHashPowerInput()).toMatch('5');
        hardwareInfoDialogPage.setPriceInput('5');
        expect(hardwareInfoDialogPage.getPriceInput()).toMatch('5');
        hardwareInfoDialogPage.videocardSelectLastOption();
        hardwareInfoDialogPage.save();
        expect(hardwareInfoDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class HardwareInfoComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-hardware-info div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class HardwareInfoDialogPage {
    modalTitle = element(by.css('h4#myHardwareInfoLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    hashPowerInput = element(by.css('input#field_hashPower'));
    priceInput = element(by.css('input#field_price'));
    videocardSelect = element(by.css('select#field_videocard'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setHashPowerInput = function (hashPower) {
        this.hashPowerInput.sendKeys(hashPower);
    }

    getHashPowerInput = function () {
        return this.hashPowerInput.getAttribute('value');
    }

    setPriceInput = function (price) {
        this.priceInput.sendKeys(price);
    }

    getPriceInput = function () {
        return this.priceInput.getAttribute('value');
    }

    videocardSelectLastOption = function () {
        this.videocardSelect.all(by.tagName('option')).last().click();
    }

    videocardSelectOption = function (option) {
        this.videocardSelect.sendKeys(option);
    }

    getVideocardSelect = function () {
        return this.videocardSelect;
    }

    getVideocardSelectedOption = function () {
        return this.videocardSelect.element(by.css('option:checked')).getText();
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
