import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  ProfitabilityAnalysisComponentsPage,
  /* ProfitabilityAnalysisDeleteDialog, */
  ProfitabilityAnalysisUpdatePage
} from './profitability-analysis.page-object';

const expect = chai.expect;

describe('ProfitabilityAnalysis e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let profitabilityAnalysisComponentsPage: ProfitabilityAnalysisComponentsPage;
  let profitabilityAnalysisUpdatePage: ProfitabilityAnalysisUpdatePage;
  /* let profitabilityAnalysisDeleteDialog: ProfitabilityAnalysisDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ProfitabilityAnalyses', async () => {
    await navBarPage.goToEntity('profitability-analysis');
    profitabilityAnalysisComponentsPage = new ProfitabilityAnalysisComponentsPage();
    await browser.wait(ec.visibilityOf(profitabilityAnalysisComponentsPage.title), 5000);
    expect(await profitabilityAnalysisComponentsPage.getTitle()).to.eq('courseworkApp.profitabilityAnalysis.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(profitabilityAnalysisComponentsPage.entities), ec.visibilityOf(profitabilityAnalysisComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ProfitabilityAnalysis page', async () => {
    await profitabilityAnalysisComponentsPage.clickOnCreateButton();
    profitabilityAnalysisUpdatePage = new ProfitabilityAnalysisUpdatePage();
    expect(await profitabilityAnalysisUpdatePage.getPageTitle()).to.eq('courseworkApp.profitabilityAnalysis.home.createOrEditLabel');
    await profitabilityAnalysisUpdatePage.cancel();
  });

  /* it('should create and save ProfitabilityAnalyses', async () => {
        const nbButtonsBeforeCreate = await profitabilityAnalysisComponentsPage.countDeleteButtons();

        await profitabilityAnalysisComponentsPage.clickOnCreateButton();

        await promise.all([
            profitabilityAnalysisUpdatePage.setDateInput('2000-12-31'),
            profitabilityAnalysisUpdatePage.userSelectLastOption(),
        ]);

        expect(await profitabilityAnalysisUpdatePage.getDateInput()).to.eq('2000-12-31', 'Expected date value to be equals to 2000-12-31');
        const selectedResult = profitabilityAnalysisUpdatePage.getResultInput();
        if (await selectedResult.isSelected()) {
            await profitabilityAnalysisUpdatePage.getResultInput().click();
            expect(await profitabilityAnalysisUpdatePage.getResultInput().isSelected(), 'Expected result not to be selected').to.be.false;
        } else {
            await profitabilityAnalysisUpdatePage.getResultInput().click();
            expect(await profitabilityAnalysisUpdatePage.getResultInput().isSelected(), 'Expected result to be selected').to.be.true;
        }

        await profitabilityAnalysisUpdatePage.save();
        expect(await profitabilityAnalysisUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await profitabilityAnalysisComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last ProfitabilityAnalysis', async () => {
        const nbButtonsBeforeDelete = await profitabilityAnalysisComponentsPage.countDeleteButtons();
        await profitabilityAnalysisComponentsPage.clickOnLastDeleteButton();

        profitabilityAnalysisDeleteDialog = new ProfitabilityAnalysisDeleteDialog();
        expect(await profitabilityAnalysisDeleteDialog.getDialogTitle())
            .to.eq('courseworkApp.profitabilityAnalysis.delete.question');
        await profitabilityAnalysisDeleteDialog.clickOnConfirmButton();

        expect(await profitabilityAnalysisComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
