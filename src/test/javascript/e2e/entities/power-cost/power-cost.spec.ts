import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PowerCostComponentsPage, PowerCostDeleteDialog, PowerCostUpdatePage } from './power-cost.page-object';

const expect = chai.expect;

describe('PowerCost e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let powerCostComponentsPage: PowerCostComponentsPage;
  let powerCostUpdatePage: PowerCostUpdatePage;
  let powerCostDeleteDialog: PowerCostDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PowerCosts', async () => {
    await navBarPage.goToEntity('power-cost');
    powerCostComponentsPage = new PowerCostComponentsPage();
    await browser.wait(ec.visibilityOf(powerCostComponentsPage.title), 5000);
    expect(await powerCostComponentsPage.getTitle()).to.eq('courseworkApp.powerCost.home.title');
    await browser.wait(ec.or(ec.visibilityOf(powerCostComponentsPage.entities), ec.visibilityOf(powerCostComponentsPage.noResult)), 1000);
  });

  it('should load create PowerCost page', async () => {
    await powerCostComponentsPage.clickOnCreateButton();
    powerCostUpdatePage = new PowerCostUpdatePage();
    expect(await powerCostUpdatePage.getPageTitle()).to.eq('courseworkApp.powerCost.home.createOrEditLabel');
    await powerCostUpdatePage.cancel();
  });

  it('should create and save PowerCosts', async () => {
    const nbButtonsBeforeCreate = await powerCostComponentsPage.countDeleteButtons();

    await powerCostComponentsPage.clickOnCreateButton();

    await promise.all([powerCostUpdatePage.setCityInput('city'), powerCostUpdatePage.setPricePerKilowattInput('5')]);

    expect(await powerCostUpdatePage.getCityInput()).to.eq('city', 'Expected City value to be equals to city');
    expect(await powerCostUpdatePage.getPricePerKilowattInput()).to.eq('5', 'Expected pricePerKilowatt value to be equals to 5');

    await powerCostUpdatePage.save();
    expect(await powerCostUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await powerCostComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last PowerCost', async () => {
    const nbButtonsBeforeDelete = await powerCostComponentsPage.countDeleteButtons();
    await powerCostComponentsPage.clickOnLastDeleteButton();

    powerCostDeleteDialog = new PowerCostDeleteDialog();
    expect(await powerCostDeleteDialog.getDialogTitle()).to.eq('courseworkApp.powerCost.delete.question');
    await powerCostDeleteDialog.clickOnConfirmButton();

    expect(await powerCostComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
