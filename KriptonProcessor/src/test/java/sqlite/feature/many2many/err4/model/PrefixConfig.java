package sqlite.feature.many2many.err4.model;

import com.abubusoft.kripton.annotation.BindType;

@BindType
public class PrefixConfig {

    public long id;

    public String defaultCountry;

    public String dualBillingPrefix;

    public boolean enabled;

    public long dialogTimeout;
}
