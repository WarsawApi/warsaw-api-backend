package contracto

import com.ghostbuster.warsawApi.controller.PropertyController
import spock.lang.Specification

public final class ContractoSpec extends Specification {

    def "ContractoSpec should pass"() {
        given:
        List<Class> apis = [
                PropertyController
        ]
        List<String> urls = [
                'https://raw.githubusercontent.com/WarsawApi/warsaw-api-contracts/master/search.contract.json',
                'https://raw.githubusercontent.com/WarsawApi/warsaw-api-contracts/master/details.contract.json',
        ]
        expect:
        new Contracto().checkContracts(apis, urls)
    }
}
