import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  HardwareInfoComponentsPage,
  /* HardwareInfoDeleteDialog, */
  HardwareInfoUpdatePage
} from './hardware-info.page-object';

const expect = chai.expect;

describe('HardwareInfo e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let hardwareInfoComponentsPage: HardwareInfoComponentsPage;
  let hardwareInfoUpdatePage: HardwareInfoUpdatePage;
  /* let hardwareInfoDeleteDialog: HardwareInfoDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load HardwareInfos', async () => {
    await navBarPage.goToEntity('hardware-info');
    hardwareInfoComponentsPage = new HardwareInfoComponentsPage();
    await browser.wait(ec.visibilityOf(hardwareInfoComponentsPage.title), 5000);
    expect(await hardwareInfoComponentsPage.getTitle()).to.eq('courseworkApp.hardwareInfo.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(hardwareInfoComponentsPage.entities), ec.visibilityOf(hardwareInfoComponentsPage.noResult)),
      1000
    );
  });

  it('should load create HardwareInfo page', async () => {
    await hardwareInfoComponentsPage.clickOnCreateButton();
    hardwareInfoUpdatePage = new HardwareInfoUpdatePage();
    expect(await hardwareInfoUpdatePage.getPageTitle()).to.eq('courseworkApp.hardwareInfo.home.createOrEditLabel');
    await hardwareInfoUpdatePage.cancel();
  });

  /* it('should create and save HardwareInfos', async () => {
        const nbButtonsBeforeCreate = await hardwareInfoComponentsPage.countDeleteButtons();

        await hardwareInfoComponentsPage.clickOnCreateButton();

        await promise.all([
            hardwareInfoUpdatePage.setHashPowerInput('5'),
            hardwareInfoUpdatePage.setPriceInput('5'),
            hardwareInfoUpdatePage.videocardSelectLastOption(),
        ]);

        expect(await hardwareInfoUpdatePage.getHashPowerInput()).to.eq('5', 'Expected hashPower value to be equals to 5');
        expect(await hardwareInfoUpdatePage.getPriceInput()).to.eq('5', 'Expected price value to be equals to 5');

        await hardwareInfoUpdatePage.save();
        expect(await hardwareInfoUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await hardwareInfoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last HardwareInfo', async () => {
        const nbButtonsBeforeDelete = await hardwareInfoComponentsPage.countDeleteButtons();
        await hardwareInfoComponentsPage.clickOnLastDeleteButton();

        hardwareInfoDeleteDialog = new HardwareInfoDeleteDialog();
        expect(await hardwareInfoDeleteDialog.getDialogTitle())
            .to.eq('courseworkApp.hardwareInfo.delete.question');
        await hardwareInfoDeleteDialog.clickOnConfirmButton();

        expect(await hardwareInfoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
