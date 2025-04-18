import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test
import main.loaders.ConfigLoader

class ConfigLoaderTest{

    @Test
    fun testLoadConfig(){
        val config = ConfigLoader.loadConfig("src/test/resources/test_config.json")
    }
}