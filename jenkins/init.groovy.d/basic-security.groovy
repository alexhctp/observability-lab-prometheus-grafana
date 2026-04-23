import hudson.security.FullControlOnceLoggedInAuthorizationStrategy
import hudson.security.HudsonPrivateSecurityRealm
import jenkins.model.Jenkins

def instance = Jenkins.get()
def username = 'admin'
def password = 'admin'

def securityRealm = instance.getSecurityRealm()
if (!(securityRealm instanceof HudsonPrivateSecurityRealm)) {
    securityRealm = new HudsonPrivateSecurityRealm(false)
    instance.setSecurityRealm(securityRealm)
}

def user = securityRealm.getUser(username)
if (user == null) {
    securityRealm.createAccount(username, password)
} else {
    def details = user.getProperty(hudson.security.HudsonPrivateSecurityRealm.Details)
    if (details == null || !details.isPasswordCorrect(password)) {
        user.addProperty(hudson.security.HudsonPrivateSecurityRealm.Details.fromPlainPassword(password))
    }
}

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStrategy(strategy)

instance.save()
