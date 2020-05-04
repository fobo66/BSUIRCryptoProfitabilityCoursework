import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CryptocurrencyComponentsPage, CryptocurrencyDeleteDialog, CryptocurrencyUpdatePage } from './cryptocurrency.page-object';

const expect = chai.expect;

describe('Cryptocurrency e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cryptocurrencyComponentsPage: CryptocurrencyComponentsPage;
  let cryptocurrencyUpdatePage: CryptocurrencyUpdatePage;
  let cryptocurrencyDeleteDialog: CryptocurrencyDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Cryptocurrencies', async () => {
    await navBarPage.goToEntity('cryptocurrency');
    cryptocurrencyComponentsPage = new CryptocurrencyComponentsPage();
    await browser.wait(ec.visibilityOf(cryptocurrencyComponentsPage.title), 5000);
    expect(await cryptocurrencyComponentsPage.getTitle()).to.eq('courseworkApp.cryptocurrency.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(cryptocurrencyComponentsPage.entities), ec.visibilityOf(cryptocurrencyComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Cryptocurrency page', async () => {
    await cryptocurrencyComponentsPage.clickOnCreateButton();
    cryptocurrencyUpdatePage = new CryptocurrencyUpdatePage();
    expect(await cryptocurrencyUpdatePage.getPageTitle()).to.eq('courseworkApp.cryptocurrency.home.createOrEditLabel');
    await cryptocurrencyUpdatePage.cancel();
  });

  it('should create and save Cryptocurrencies', async () => {
    const nbButtonsBeforeCreate = await cryptocurrencyComponentsPage.countDeleteButtons();

    await cryptocurrencyComponentsPage.clickOnCreateButton();

    await promise.all([
      cryptocurrencyUpdatePage.setNameInput('name'),
      cryptocurrencyUpdatePage.setShortNameInput('shortName'),
      cryptocurrencyUpdatePage.setPriceInput('5')
    ]);

    expect(await cryptocurrencyUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await cryptocurrencyUpdatePage.getShortNameInput()).to.eq('shortName', 'Expected ShortName value to be equals to shortName');
    expect(await cryptocurrencyUpdatePage.getPriceInput()).to.eq('5', 'Expected price value to be equals to 5');

    await cryptocurrencyUpdatePage.save();
    expect(await cryptocurrencyUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cryptocurrencyComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last Cryptocurrency', async () => {
    const nbButtonsBeforeDelete = await cryptocurrencyComponentsPage.countDeleteButtons();
    await cryptocurrencyComponentsPage.clickOnLastDeleteButton();

    cryptocurrencyDeleteDialog = new CryptocurrencyDeleteDialog();
    expect(await cryptocurrencyDeleteDialog.getDialogTitle()).to.eq('courseworkApp.cryptocurrency.delete.question');
    await cryptocurrencyDeleteDialog.clickOnConfirmButton();

    expect(await cryptocurrencyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
