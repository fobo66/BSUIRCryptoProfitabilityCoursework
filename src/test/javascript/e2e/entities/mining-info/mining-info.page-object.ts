import { element, by, ElementFinder } from 'protractor';

export class MiningInfoComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-mining-info div table .btn-danger'));
  title = element.all(by.css('jhi-mining-info div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class MiningInfoUpdatePage {
  pageTitle = element(by.id('jhi-mining-info-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  difficultyInput = element(by.id('field_difficulty'));
  blockRewardInput = element(by.id('field_blockReward'));

  cryptocurrencySelect = element(by.id('field_cryptocurrency'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setDifficultyInput(difficulty: string): Promise<void> {
    await this.difficultyInput.sendKeys(difficulty);
  }

  async getDifficultyInput(): Promise<string> {
    return await this.difficultyInput.getAttribute('value');
  }

  async setBlockRewardInput(blockReward: string): Promise<void> {
    await this.blockRewardInput.sendKeys(blockReward);
  }

  async getBlockRewardInput(): Promise<string> {
    return await this.blockRewardInput.getAttribute('value');
  }

  async cryptocurrencySelectLastOption(): Promise<void> {
    await this.cryptocurrencySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async cryptocurrencySelectOption(option: string): Promise<void> {
    await this.cryptocurrencySelect.sendKeys(option);
  }

  getCryptocurrencySelect(): ElementFinder {
    return this.cryptocurrencySelect;
  }

  async getCryptocurrencySelectedOption(): Promise<string> {
    return await this.cryptocurrencySelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class MiningInfoDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-miningInfo-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-miningInfo'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
