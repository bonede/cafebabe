import {Base64File} from "../api/ApiClient";
import JSZip from "jszip";
import {saveAs} from 'file-saver';
import {Intent, Toaster, ToasterInstance} from "@blueprintjs/core";

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

let toaster: ToasterInstance | undefined = undefined

export function showToast(message: string, intent?: Intent){
    if(toaster === undefined){
        toaster = Toaster.create()
    }
    toaster.show({
        message: message,
        intent
    })
}

export function getShareId(url: string){
    const u = new URL(window.location.href)
    return u.searchParams.get('s')
}
