import { element, by, ElementFinder } from 'protractor';

export class PowerCostComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-power-cost div table .btn-danger'));
  title = element.all(by.css('jhi-power-cost div h2#page-heading span')).first();
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

export class PowerCostUpdatePage {
  pageTitle = element(by.id('jhi-power-cost-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  cityInput = element(by.id('field_city'));
  pricePerKilowattInput = element(by.id('field_pricePerKilowatt'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCityInput(city: string): Promise<void> {
    await this.cityInput.sendKeys(city);
  }

  async getCityInput(): Promise<string> {
    return await this.cityInput.getAttribute('value');
  }

  async setPricePerKilowattInput(pricePerKilowatt: string): Promise<void> {
    await this.pricePerKilowattInput.sendKeys(pricePerKilowatt);
  }

  async getPricePerKilowattInput(): Promise<string> {
    return await this.pricePerKilowattInput.getAttribute('value');
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

export class PowerCostDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-powerCost-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-powerCost'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
