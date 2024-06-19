import { WebPlugin } from '@capacitor/core';
export class WifiWeb extends WebPlugin {
    constructor() {
        super();
        window.screen.orientation.addEventListener("change", () => {
            const type = window.screen.orientation.type;
            console.log("Changement de l'orientation de l'Ecran: ", type);
            this.notifyListeners("screenOrientationChange", { type });
        });
    }
    async getIP() {
        const reponse = await this.getLocalIP().then((ipRecus) => {
            return { ip: ipRecus };
        }, (err) => {
            console.error("ERR getIp pour le Web: ", err);
            return { ip: "" };
        });
        return reponse;
    }
    async getSSID() {
        return { ssid: null };
    }
    async connect(options) {
        console.log(options);
        return { ssid: null };
    }
    async connectPrefix(options) {
        console.log(options);
        return { ssid: null };
    }
    async disconnect() {
        return;
    }
    /**
     * Get Local IP Address
     *
     * @returns Promise Object
     *
     * getLocalIP().then((ipAddr) => {
     *    console.log(ipAddr); // 192.168.0.122
     * });
     */
    getLocalIP() {
        return new Promise(function (resolve, reject) {
            // NOTE: window.RTCPeerConnection is "not a constructor" in FF22/23
            console.log("Recherche LocalIP avec JavaScript...");
            const RTCPeerConnection = window.RTCPeerConnection; // window.webkitRTCPeerConnection || window.mozRTCPeerConnection;
            if (!RTCPeerConnection) {
                reject('Your browser does not support this API');
            }
            const rtc = new RTCPeerConnection({ iceServers: [] });
            //const addrs = {};
            //let addrs["0.0.0.0"] = false;
            const grepSDP = (sdp) => {
                //let hosts = [];
                let finalIP = '';
                sdp.split('\r\n').forEach(function (line) {
                    if (~line.indexOf("a=candidate")) { // http://tools.ietf.org/html/rfc4566#section-5.13
                        const parts = line.split(' '), // http://tools.ietf.org/html/rfc5245#section-15.1
                        addr = parts[4], type = parts[7];
                        console.log("IP_: ", addr);
                        if (type === 'host') {
                            finalIP = addr;
                        }
                    }
                    else if (~line.indexOf("c=")) { // http://tools.ietf.org/html/rfc4566#section-5.7
                        console.log("IP type2: JavaScript...", line);
                        const parts = line.split(' '), addr = parts[2];
                        finalIP = addr;
                    }
                });
                return finalIP;
            };
            // if (1 || window.mozRTCPeerConnection) {      // FF [and now Chrome!] needs a channel/stream to proceed
            //     rtc.createDataChannel('', {reliable:false});
            // };
            rtc.onicecandidate = function (evt) {
                console.log("Execution en JavaScript...", evt);
                // convert the candidate to SDP so we can run it through our general parser
                // see https://twitter.com/lancestout/status/525796175425720320 for details
                if (evt.candidate) {
                    const addr = grepSDP("a=" + evt.candidate.candidate);
                    resolve(addr);
                }
            };
            rtc.createOffer();
            //rtc.createOffer();
        });
    }
}
//# sourceMappingURL=web.js.map