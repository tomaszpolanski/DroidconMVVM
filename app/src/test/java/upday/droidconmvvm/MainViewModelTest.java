package upday.droidconmvvm;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;
import upday.droidconmvvm.datamodel.IDataModel;
import upday.droidconmvvm.model.Language;

import static upday.droidconmvvm.model.Language.LanguageCode;

public class MainViewModelTest {

    @Mock
    private IDataModel mDataModel;

    private MainViewModel mMainViewModel;
    
    private static final Language GERMAN = new Language("German", LanguageCode.DE);
    private static final Language ENGLISH = new Language("English", LanguageCode.EN);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mMainViewModel = new MainViewModel(mDataModel);
    }

    @Test
    public void testGetGreeting_doesNotEmit_whenNoLanguageSet1() {
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        mMainViewModel.getGreeting().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertNoValues();
    }

    @Test
    public void testGetGreeting_doesNotEmit_whenNoLanguageSet2() {
        TestSubscriber<String> testSubscriber = subscribeTs(mMainViewModel.getGreeting());

        testSubscriber.assertNoTerminalEvent();
    }

    @Test
    public void testGetSupportedLanguages_emitsCorrectLanguages1() {
        Language de = new Language("German", LanguageCode.DE);
        Language en = new Language("English", LanguageCode.EN);
        List<Language> languages = Arrays.asList(de, en);
        Mockito.when(mDataModel.getSupportedLanguages()).thenReturn(Observable.just(languages));
        TestSubscriber<List<Language>> testSubscriber = new TestSubscriber<>();

        mMainViewModel.getSupportedLanguages().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(languages);
    }

    @Test
    public void testGetSupportedLanguages_emitsCorrectLanguages2() {
        List<Language> languages = Arrays.asList(GERMAN, ENGLISH);
        new ArrangeBuilder().withLanguages(languages);

        TestSubscriber<List<Language>> testSubscriber = subscribeTs(mMainViewModel.getSupportedLanguages());

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(languages);
    }


    private static <T> TestSubscriber<T> subscribeTs(Observable<T> observable) {
        TestSubscriber<T> testSubscriber = new TestSubscriber<>();
        observable.subscribe(testSubscriber);
        return testSubscriber;
    }

    private class ArrangeBuilder {

        ArrangeBuilder withLanguages(List<Language> languages) {
            Mockito.when(mDataModel.getSupportedLanguages())
                    .thenReturn(Observable.just(languages));
            return this;
        }
    }

}

