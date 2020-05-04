import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  MiningInfoComponentsPage,
  /* MiningInfoDeleteDialog, */
  MiningInfoUpdatePage
} from './mining-info.page-object';

const expect = chai.expect;

describe('MiningInfo e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let miningInfoComponentsPage: MiningInfoComponentsPage;
  let miningInfoUpdatePage: MiningInfoUpdatePage;
  /* let miningInfoDeleteDialog: MiningInfoDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load MiningInfos', async () => {
    await navBarPage.goToEntity('mining-info');
    miningInfoComponentsPage = new MiningInfoComponentsPage();
    await browser.wait(ec.visibilityOf(miningInfoComponentsPage.title), 5000);
    expect(await miningInfoComponentsPage.getTitle()).to.eq('courseworkApp.miningInfo.home.title');
    await browser.wait(ec.or(ec.visibilityOf(miningInfoComponentsPage.entities), ec.visibilityOf(miningInfoComponentsPage.noResult)), 1000);
  });

  it('should load create MiningInfo page', async () => {
    await miningInfoComponentsPage.clickOnCreateButton();
    miningInfoUpdatePage = new MiningInfoUpdatePage();
    expect(await miningInfoUpdatePage.getPageTitle()).to.eq('courseworkApp.miningInfo.home.createOrEditLabel');
    await miningInfoUpdatePage.cancel();
  });

  /* it('should create and save MiningInfos', async () => {
        const nbButtonsBeforeCreate = await miningInfoComponentsPage.countDeleteButtons();

        await miningInfoComponentsPage.clickOnCreateButton();

        await promise.all([
            miningInfoUpdatePage.setDifficultyInput('5'),
            miningInfoUpdatePage.setBlockRewardInput('5'),
            miningInfoUpdatePage.cryptocurrencySelectLastOption(),
        ]);

        expect(await miningInfoUpdatePage.getDifficultyInput()).to.eq('5', 'Expected difficulty value to be equals to 5');
        expect(await miningInfoUpdatePage.getBlockRewardInput()).to.eq('5', 'Expected blockReward value to be equals to 5');

        await miningInfoUpdatePage.save();
        expect(await miningInfoUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await miningInfoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last MiningInfo', async () => {
        const nbButtonsBeforeDelete = await miningInfoComponentsPage.countDeleteButtons();
        await miningInfoComponentsPage.clickOnLastDeleteButton();

        miningInfoDeleteDialog = new MiningInfoDeleteDialog();
        expect(await miningInfoDeleteDialog.getDialogTitle())
            .to.eq('courseworkApp.miningInfo.delete.question');
        await miningInfoDeleteDialog.clickOnConfirmButton();

        expect(await miningInfoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
