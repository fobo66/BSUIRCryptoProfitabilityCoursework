import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('MiningInfo e2e test', () => {

    let navBarPage: NavBarPage;
    let miningInfoDialogPage: MiningInfoDialogPage;
    let miningInfoComponentsPage: MiningInfoComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load MiningInfos', () => {
        navBarPage.goToEntity('mining-info');
        miningInfoComponentsPage = new MiningInfoComponentsPage();
        expect(miningInfoComponentsPage.getTitle()).toMatch(/courseworkApp.miningInfo.home.title/);

    });

    it('should load create MiningInfo dialog', () => {
        miningInfoComponentsPage.clickOnCreateButton();
        miningInfoDialogPage = new MiningInfoDialogPage();
        expect(miningInfoDialogPage.getModalTitle()).toMatch(/courseworkApp.miningInfo.home.createOrEditLabel/);
        miningInfoDialogPage.close();
    });

   /* it('should create and save MiningInfos', () => {
        miningInfoComponentsPage.clickOnCreateButton();
        miningInfoDialogPage.setDifficultyInput('5');
        expect(miningInfoDialogPage.getDifficultyInput()).toMatch('5');
        miningInfoDialogPage.setBlockRewardInput('5');
        expect(miningInfoDialogPage.getBlockRewardInput()).toMatch('5');
        miningInfoDialogPage.cryptocurrencySelectLastOption();
        miningInfoDialogPage.save();
        expect(miningInfoDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class MiningInfoComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-mining-info div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class MiningInfoDialogPage {
    modalTitle = element(by.css('h4#myMiningInfoLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    difficultyInput = element(by.css('input#field_difficulty'));
    blockRewardInput = element(by.css('input#field_blockReward'));
    cryptocurrencySelect = element(by.css('select#field_cryptocurrency'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setDifficultyInput = function (difficulty) {
        this.difficultyInput.sendKeys(difficulty);
    }

    getDifficultyInput = function () {
        return this.difficultyInput.getAttribute('value');
    }

    setBlockRewardInput = function (blockReward) {
        this.blockRewardInput.sendKeys(blockReward);
    }

    getBlockRewardInput = function () {
        return this.blockRewardInput.getAttribute('value');
    }

    cryptocurrencySelectLastOption = function () {
        this.cryptocurrencySelect.all(by.tagName('option')).last().click();
    }

    cryptocurrencySelectOption = function (option) {
        this.cryptocurrencySelect.sendKeys(option);
    }

    getCryptocurrencySelect = function () {
        return this.cryptocurrencySelect;
    }

    getCryptocurrencySelectedOption = function () {
        return this.cryptocurrencySelect.element(by.css('option:checked')).getText();
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
