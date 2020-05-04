import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { VideocardComponentsPage, VideocardDeleteDialog, VideocardUpdatePage } from './videocard.page-object';

const expect = chai.expect;

describe('Videocard e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let videocardComponentsPage: VideocardComponentsPage;
  let videocardUpdatePage: VideocardUpdatePage;
  let videocardDeleteDialog: VideocardDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Videocards', async () => {
    await navBarPage.goToEntity('videocard');
    videocardComponentsPage = new VideocardComponentsPage();
    await browser.wait(ec.visibilityOf(videocardComponentsPage.title), 5000);
    expect(await videocardComponentsPage.getTitle()).to.eq('courseworkApp.videocard.home.title');
    await browser.wait(ec.or(ec.visibilityOf(videocardComponentsPage.entities), ec.visibilityOf(videocardComponentsPage.noResult)), 1000);
  });

  it('should load create Videocard page', async () => {
    await videocardComponentsPage.clickOnCreateButton();
    videocardUpdatePage = new VideocardUpdatePage();
    expect(await videocardUpdatePage.getPageTitle()).to.eq('courseworkApp.videocard.home.createOrEditLabel');
    await videocardUpdatePage.cancel();
  });

  it('should create and save Videocards', async () => {
    const nbButtonsBeforeCreate = await videocardComponentsPage.countDeleteButtons();

    await videocardComponentsPage.clickOnCreateButton();

    await promise.all([videocardUpdatePage.setNameInput('name'), videocardUpdatePage.setPowerInput('5')]);

    expect(await videocardUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await videocardUpdatePage.getPowerInput()).to.eq('5', 'Expected power value to be equals to 5');

    await videocardUpdatePage.save();
    expect(await videocardUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await videocardComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Videocard', async () => {
    const nbButtonsBeforeDelete = await videocardComponentsPage.countDeleteButtons();
    await videocardComponentsPage.clickOnLastDeleteButton();

    videocardDeleteDialog = new VideocardDeleteDialog();
    expect(await videocardDeleteDialog.getDialogTitle()).to.eq('courseworkApp.videocard.delete.question');
    await videocardDeleteDialog.clickOnConfirmButton();

    expect(await videocardComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
