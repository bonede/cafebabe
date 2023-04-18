import {Base64File} from "../api/ApiClient";
import JSZip from "jszip";
import {saveAs} from 'file-saver';

export function addClass(ele: HTMLElement, className: string){
    if(!ele.className.split(" ").includes(className)){
        ele.className = ele.className + " " + className;
    }
}

export function removeClass(ele: HTMLElement, className: string){
    ele.className = ele.className.replace(new RegExp("\\s*" + className), "")
}

export function createDiv(className: string): HTMLDivElement{
    let div = document.createElement("div")
    div.className = className
    return div
}

export async function saveZipFile(zipFilename: string, files: Base64File[]) {
    const zip = new JSZip();
    files.forEach(f => {
        zip.file(f.path, f.content, {base64: true});
    })

    const blob = await zip.generateAsync({type: "blob"})
    saveAs(blob, zipFilename);
}
