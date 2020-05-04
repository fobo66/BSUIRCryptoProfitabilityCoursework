import { element, by, ElementFinder } from 'protractor';

export class HardwareInfoComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-hardware-info div table .btn-danger'));
  title = element.all(by.css('jhi-hardware-info div h2#page-heading span')).first();
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

export class HardwareInfoUpdatePage {
  pageTitle = element(by.id('jhi-hardware-info-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  hashPowerInput = element(by.id('field_hashPower'));
  priceInput = element(by.id('field_price'));

  videocardSelect = element(by.id('field_videocard'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setHashPowerInput(hashPower: string): Promise<void> {
    await this.hashPowerInput.sendKeys(hashPower);
  }

  async getHashPowerInput(): Promise<string> {
    return await this.hashPowerInput.getAttribute('value');
  }

  async setPriceInput(price: string): Promise<void> {
    await this.priceInput.sendKeys(price);
  }

  async getPriceInput(): Promise<string> {
    return await this.priceInput.getAttribute('value');
  }

  async videocardSelectLastOption(): Promise<void> {
    await this.videocardSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async videocardSelectOption(option: string): Promise<void> {
    await this.videocardSelect.sendKeys(option);
  }

  getVideocardSelect(): ElementFinder {
    return this.videocardSelect;
  }

  async getVideocardSelectedOption(): Promise<string> {
    return await this.videocardSelect.element(by.css('option:checked')).getText();
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

export class HardwareInfoDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-hardwareInfo-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-hardwareInfo'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
