import {addClass, createDiv} from "../Utils";
import './index.css'

export class Logger{
    rootDiv: HTMLDivElement

    constructor(rootDiv: HTMLDivElement) {
        this.rootDiv = rootDiv
        this.rootDiv.className = "pl-logger"
    }

    public appendLog(level: "info" | "error", data: string){
        let div = createDiv("pl-logger-item")
        addClass(div, level)
        div.innerText = data
        this.rootDiv.appendChild(div)
        this.rootDiv.scrollTop = this.rootDiv.scrollHeight;
    }

    public setHeight(height: number){
        this.rootDiv.style.height = height + "px"
    }

    public info(data: string){
        this.appendLog("info", data)
    }

    public error(data: string){
        this.appendLog("error", data)
    }
}