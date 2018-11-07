function getCookie(name) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
}

function set_cookie(name, value) {
    document.cookie = name +'='+ value +'; Path=/;';
}
function delete_cookie(name) {
    document.cookie = name +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function isAnonymous(name){
    return name == "anonimousUser";
}
window.Event = new Vue({
    data: {isLoggedIn: false}
});

var vueLog= new Vue({
    el: '#loggedIn',
    data: {logged_in_msg: ""},//ako postoi poraka, ke ja displayuva vo nav.html so paragrafot+id="loggedIn"
    mounted(){
        var isLoggedIn= false;//inicijalno setirame a podolu proveruvame ako ima getCookie
    }
});
//da pratime POST reguest I obtain token vo login.Co v-model vo index, gi mapirame so data od Vue
// Vo metods go imame login() koj ke praka POST koristejki go axios so shto go dobivame access tokenot
// following POST requestot to the URL (aouth/token)
Vue.component('login-component',{//dodavame link za registration
    template: '<ul class="navMenu" v-if="!isLoggedIn()" style="float:right"><li style="margin-right:5px"><a href="/login">Login</a></li>' +
    '<li><a href="/registration">Register</a></li></ul>' +
    '<p v-else id="loggedIn">{{logged_in_msg}} <span v-on:click="logOut" v-show="isLoggedIn()"><b style="color:#2eafda;">Logout</b></span></p>',
    data: function(){
        return {logged_in_msg : ""}
    },
    mounted(){
        if(getCookie("access_token")){
            axios.get("/getUsername?access_token=" + getCookie("access_token"))//go zemame UserName so accessToken
                .then(function(response){//koj ni go provide getCookie(), ke kreirame () shto ke gi BIND
                    this.logged_in_msg = "Welcome back , " + response.data;//ja setirame porakata=
                    window.Event.isLoggedIn = true;//otkako dobivme pozitiven response od serverot
                    Event.$emit('logged-in');
                }.bind(this))
                .catch(function(error){
                    delete_cookie("access_token");
                    return error;
                });
        }
    },

    methods : {
        logOut(){
            axios.get("/logouts?access_token="+getCookie("access_token"))//logout () bara parameter accessToken koj e = na getCookie(accessToken)
                .then(function(response){//otkako ovaj GETrequest e napraen, setirame var isLoggedIn=false+poraka+delete cookie
                    window.Event.isLoggedIn = false;
                    this.logged_in_msg  = "Successfully logged out";
                    delete_cookie("access_token")//za koga refreshirame da ne probuva da ne najavuva so istiot token
                }.bind(this))
        },
        isLoggedIn(){
            return window.Event.isLoggedIn;
        }
    }
});