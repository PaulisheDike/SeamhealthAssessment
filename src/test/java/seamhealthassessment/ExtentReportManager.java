package seamhealthassessment;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient.Request.Method;
//import java.lang.reflect.Method;


public class ExtentReportManager {
	
	private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    //DockerHttpClient.Request.Method httpMethod = DockerHttpClient.Request.Method.GET;
    //System.out.println(httpMethod.toString()); // Example method for DockerHttpClient


//    @BeforeSuite
//    public void beforeMethod(Method method, ITestContext context) {
//        String testName = method.getDeclaringClass() + " - " + context.getCurrentXmlTest().getParameter("browser");
//        ExtentTest extentTest = extent.createTest(testName);
//        test.set(extentTest);
//    }
    
    @BeforeSuite
    public void beforeSuite() {
    	ExtentSparkReporter htmlReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        htmlReporter.config().setDocumentTitle("Seamhealth Assessment");
        htmlReporter.config().setReportName("Functional Test Automation Report");
        htmlReporter.config().setTheme(Theme.DARK);
    }

    @BeforeMethod
    public void beforeMethod() {
        ExtentTest extentTest = extent.createTest(getClass().getSimpleName() + " - " + Thread.currentThread().getName());
        test.set(extentTest);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.get().fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.get().pass("Test passed");
        } else {
            test.get().skip("Test skipped");
        }
    }

    @AfterSuite
    public void afterSuite() {
        extent.flush();
    }

}
