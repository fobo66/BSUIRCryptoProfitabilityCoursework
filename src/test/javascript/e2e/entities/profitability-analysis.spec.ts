import {browser, element, by, $} from 'protractor';
import {NavBarPage} from './../page-objects/jhi-page-objects';

const path = require('path');

describe('ProfitabilityAnalysis e2e test', () => {

    let navBarPage: NavBarPage;
    let profitabilityAnalysisDialogPage: ProfitabilityAnalysisDialogPage;
    let profitabilityAnalysisComponentsPage: ProfitabilityAnalysisComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);


    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load ProfitabilityAnalyses', () => {
        navBarPage.goToEntity('profitability-analysis');
        profitabilityAnalysisComponentsPage = new ProfitabilityAnalysisComponentsPage();
        expect(profitabilityAnalysisComponentsPage.getTitle()).toMatch(/courseworkApp.profitabilityAnalysis.home.title/);

    });

    /* it('should create and save ProfitabilityAnalyses', () => {
         profitabilityAnalysisComponentsPage.clickOnCreateButton();
         profitabilityAnalysisDialogPage.setDateInput('2000-12-31');
         expect(profitabilityAnalysisDialogPage.getDateInput()).toMatch('2000-12-31');
         profitabilityAnalysisDialogPage.getResultInput().isSelected().then(function (selected) {
             if (selected) {
                 profitabilityAnalysisDialogPage.getResultInput().click();
                 expect(profitabilityAnalysisDialogPage.getResultInput().isSelected()).toBeFalsy();
             } else {
                 profitabilityAnalysisDialogPage.getResultInput().click();
                 expect(profitabilityAnalysisDialogPage.getResultInput().isSelected()).toBeTruthy();
             }
         });
         profitabilityAnalysisDialogPage.userSelectLastOption();
         profitabilityAnalysisDialogPage.save();
         expect(profitabilityAnalysisDialogPage.getSaveButton().isPresent()).toBeFalsy();
     }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ProfitabilityAnalysisComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-profitability-analysis div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ProfitabilityAnalysisDialogPage {
    modalTitle = element(by.css('h4#myProfitabilityAnalysisLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    dateInput = element(by.css('input#field_date'));
    resultInput = element(by.css('input#field_result'));
    userSelect = element(by.css('select#field_user'));
    setDateInput = function (date) {
        this.dateInput.sendKeys(date);
    }
    getDateInput = function () {
        return this.dateInput.getAttribute('value');
    }
    getResultInput = function () {
        return this.resultInput;
    }
    userSelectLastOption = function () {
        this.userSelect.all(by.tagName('option')).last().click();
    }
    userSelectOption = function (option) {
        this.userSelect.sendKeys(option);
    }
    getUserSelect = function () {
        return this.userSelect;
    }
    getUserSelectedOption = function () {
        return this.userSelect.element(by.css('option:checked')).getText();
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
