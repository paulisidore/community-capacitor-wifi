import { WebPlugin } from '@capacitor/core';

import type { WifiPlugin } from './definitions';

export class WifiWeb extends WebPlugin implements WifiPlugin {

  constructor (){
    super();
    window.screen.orientation.addEventListener("change", () => {      
      const type = window.screen.orientation.type;
      console.log("Changement de l'orientation de l'Ecran: ", type);
      this.notifyListeners("screenOrientationChange", { type });
    });
  }

  async getIP(): Promise<{ ip: string | null }> {
    return { ip: null };
  }
  
  async getSSID(): Promise<{ssid: string | null}> {
    return { ssid: null }
  }

  async connect(options: { ssid: string, password?: string, authType?: string }): Promise<{ ssid: string | null }> {
    console.log(options);
    return { ssid: null };
  }
  async connectPrefix(options: { ssid: string, password?: string, authType?: string }): Promise<{ ssid: string | null }> {
    console.log(options);
    return { ssid: null };
  }
  async disconnect(): Promise<void> {
    return;
  }
}