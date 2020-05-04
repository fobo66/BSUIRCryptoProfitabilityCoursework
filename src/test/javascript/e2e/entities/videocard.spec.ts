import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Videocard e2e test', () => {

    let navBarPage: NavBarPage;
    let videocardDialogPage: VideocardDialogPage;
    let videocardComponentsPage: VideocardComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Videocards', () => {
        navBarPage.goToEntity('videocard');
        videocardComponentsPage = new VideocardComponentsPage();
        expect(videocardComponentsPage.getTitle()).toMatch(/courseworkApp.videocard.home.title/);

    });

    it('should load create Videocard dialog', () => {
        videocardComponentsPage.clickOnCreateButton();
        videocardDialogPage = new VideocardDialogPage();
        expect(videocardDialogPage.getModalTitle()).toMatch(/courseworkApp.videocard.home.createOrEditLabel/);
        videocardDialogPage.close();
    });

    it('should create and save Videocards', () => {
        videocardComponentsPage.clickOnCreateButton();
        videocardDialogPage.setNameInput('name');
        expect(videocardDialogPage.getNameInput()).toMatch('name');
        videocardDialogPage.setPowerInput('5');
        expect(videocardDialogPage.getPowerInput()).toMatch('5');
        videocardDialogPage.save();
        expect(videocardDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class VideocardComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-videocard div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class VideocardDialogPage {
    modalTitle = element(by.css('h4#myVideocardLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    powerInput = element(by.css('input#field_power'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function (name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function () {
        return this.nameInput.getAttribute('value');
    }

    setPowerInput = function (power) {
        this.powerInput.sendKeys(power);
    }

    getPowerInput = function () {
        return this.powerInput.getAttribute('value');
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
