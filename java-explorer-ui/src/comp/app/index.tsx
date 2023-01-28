import './index.css'
import {Direction, Panel, PanelGroup, PanelTab} from "../panel";
import React, {useEffect, useState} from "react";
import {getPanelElement, isMouseOnHandleBar, resize} from "../rect";
import {addClass, removeClass} from "../Utils";
import {Editor} from "../editor";
import {ApiClient, AppInfo, CompilerInfo} from "../../api/ApiClient";


export function JavaExplorerApp(){

    let resizing = false
    let startX = 0
    let startY = 0
    let lastX = 0
    let lastY = 0
    let startFlex = 0
    let lastWidth = 0
    let lastHeight = 0
    let panelEle = null as HTMLElement | null
    let [appInfo, setAppInfo] = useState<AppInfo>()
    let [currentCompiler, setCurrentCompiler] = useState<CompilerInfo>()
    let [editorContent, setEditorContent] = useState<string>("")
    
    const handleWindowMouseMove = (e: MouseEvent) => {
        if(resizing){
            resize(panelEle!, e.clientX, e.clientY, startX, startY, lastX, lastY, startFlex)
            lastX = e.clientX
            lastY = e.clientY
            return
        }
        const showHandleBar = isMouseOnHandleBar(e);
        const thisPanelEle = getPanelElement(e)
        if(showHandleBar){
            addClass(thisPanelEle!, "hover")
        }else {
            removeClass(thisPanelEle!, "hover")
        }

    }

    const handleWindowMouseDown = (e: MouseEvent) => {
        if(isMouseOnHandleBar(e)){
            resizing = true;
            panelEle = getPanelElement(e)!
            startX = e.clientX
            startY = e.clientY
            lastWidth = panelEle.offsetWidth
            lastHeight = panelEle.offsetHeight
            console.warn("BEGIN")
            startFlex = parseFloat(getComputedStyle(getPanelElement(e)!).flexGrow)
        }
    }

    const handleWindowMouseUp = (e: MouseEvent) => {
        resizing = false;
        panelEle = null
        startX = 0
        startY = 0
        startFlex = 0
    }


    useEffect(()=> {
        window.addEventListener("mousemove", handleWindowMouseMove)
        window.addEventListener("mousedown", handleWindowMouseDown)
        window.addEventListener("mouseup", handleWindowMouseUp)
        ApiClient.getClient().getAppInfo().then(r => {
            setAppInfo(r)
            setCurrentCompiler(r.compilers[0])
        })
    }, [])


    const handleLangSelect: React.ChangeEventHandler<HTMLSelectElement> = (e) => {
        setCurrentCompiler(appInfo?.compilers.filter(c => c.name == e.target.value)[0])
    }
    const handleEditorContentChange = (content: string) => {
        setEditorContent(content);
    }
    const editorPanel = <Panel
                    minWidth={400}
                    right={<select onChange={handleLangSelect}>{appInfo && appInfo.compilers.map(c => <option>{c.name}</option> )}</select>}
                    rightIcons={
                    [
                        {
                            tip: "Build",
                            icon: "hammer.svg"
                        },
                        {
                          tip: "Upload",
                          icon: "cloud_upload.svg"
                        },
                        {
                          tip: "Share",
                          icon: "link.svg"
                        }
                    ]
                    } showTitle={true} showFooter={true} size={1}>
                        <PanelTab footer="" title={currentCompiler && currentCompiler.fileName}>
                            <Editor onContentChange={handleEditorContentChange} lang={currentCompiler && currentCompiler.lang || "java"} content={currentCompiler && currentCompiler.example || ""} />
                        </PanelTab>
                </Panel>

    const right = <PanelGroup direction={Direction.Vertical} sizes={[1]}>
        <Panel showTitle={true} showFooter={true} size={1}>
            <PanelTab title={"Main.class"}>12</PanelTab>
        </Panel>
        <Panel showTitle={true} showFooter={true} size={1}>
            <PanelTab title={"Output"}>12</PanelTab>
        </Panel>
    </PanelGroup>
    return <PanelGroup direction={Direction.Horizontal} sizes={[1]}>
        {editorPanel}
        {right}
    </PanelGroup>

}