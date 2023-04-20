import './app.css'
import React, {createContext, useEffect, useState} from "react";
import {ApiClient, AppInfo, ClassFile, CompileResult, CompilerOps, ShareResp, SrcFile} from "../../api/ApiClient";
import {ClassFileWindow} from "../classFileWindow/ClassFileWindow";
import {OutputMsg, OutputType, OutputWindow} from "../outputWindow/OutputWindow";
import {EditorWindow} from "../editorWindow/EditorWindow";
import {Panel, PanelGroup, PanelResizeHandle} from "react-resizable-panels";
import {Button, Dialog, DialogBody, DialogFooter} from "@blueprintjs/core";
import {CopyButton} from "../copyButton/CopyButton";

export const AppInfoContext = createContext(undefined as AppInfo | undefined)

export const JavaExplorerApp = () => {
    const apiClient = ApiClient.getClient()
    const [selectedFile, setSelectedFile] = useState(undefined as string | undefined)
    const [selectedLines, setSelectedLines] = useState([] as number[])
    const [outputMsgs, setOutputMsg] = useState([] as OutputMsg[])
    const [classFiles, setClassFiles] = useState([] as ClassFile[])
    const [appInfo, setAppInfo] = useState(undefined as AppInfo | undefined)
    const [shareResp, setShareResp] = useState(undefined as ShareResp | undefined)
    const [classFileName, setClassFileName] = useState(undefined as string | undefined)
    const [classFileLine, setClassFileLine] = useState(undefined as number | undefined)
    useEffect(() => {
        apiClient.getAppInfo().then(a => setAppInfo(a))
    }, [])
    const pushMsg = (type: OutputType, msg: string) => {
        setOutputMsg([...outputMsgs, {
            type,
            msg
        }])
    }
    const handleCompile = (result: CompileResult) => {
        if(result.classFiles != null){
            setClassFiles(result.classFiles)
        }
        if(result.stdout){
            pushMsg("stdout", result.stdout)
        }
        if(result.stderr){
            pushMsg("stderr", result.stderr)
        }
    }
    const handleClearMsg = () => {
        setOutputMsg([])
    }

    const handleSelectClassFileLine = (file: string, line?: number) => {
        setClassFileLine(line)
        setClassFileName(file)
    }

    if(!appInfo){
        return <div></div>
    }

    const handleShareReq = async (ops: CompilerOps, srcFiles: SrcFile[], hoursToLive?: number) => {
        try {
            const shareResp = await apiClient.createShare(ops, srcFiles, hoursToLive)
            setShareResp(shareResp)
        } catch (e) {
            pushMsg("stderr", e + "")
        }
    }
    return <AppInfoContext.Provider value={appInfo}>
     <div id="app" className="mosaic-blueprint-theme bp4-dark mosaic">
         <Dialog onClose={() => setShareResp(undefined)} isOpen={shareResp !== undefined} title="Share"  className="bp4-dark">
             <DialogBody>
                 <div className="bp4-text-large">
                     <p>Share Link</p>
                     <p className="bp4-monospace-text"><a href={shareResp?.url} target="_blank">{shareResp?.url}</a>  <CopyButton content={shareResp?.url} /></p>
                     <p>Deleting Token</p>
                     <p className="bp4-monospace-text">{shareResp?.deletingToken} <CopyButton content={shareResp?.deletingToken} /></p>
                 </div>

             </DialogBody>
             <DialogFooter actions={<Button intent="primary" text="Okay" onClick={() => setShareResp(undefined)} />} />
         </Dialog>
         <PanelGroup direction="horizontal">
             <Panel defaultSize={45}>
                 <PanelGroup direction="vertical">
                     <Panel>
                         <EditorWindow onShareReq={handleShareReq} selectLine={classFileLine} onSelectLines={lines => setSelectedLines(lines)} onCompile={handleCompile} compilers={appInfo?.compilers || []} />
                     </Panel>
                     <PanelResizeHandle><div style={{background: "#00000000", height: 5}}></div></PanelResizeHandle>
                     <Panel  defaultSize={30}>
                         <OutputWindow outputMsgs={outputMsgs} onClearMsg={handleClearMsg} />
                     </Panel>
                 </PanelGroup>
             </Panel>
             <PanelResizeHandle><div style={{background: "#00000000", width: 5, height: "100%"}}></div></PanelResizeHandle>
             <Panel>
                 <ClassFileWindow onSelectLine={handleSelectClassFileLine} classFiles={classFiles} selectedFile={selectedFile} selectedLines={selectedLines} />
             </Panel>
         </PanelGroup>
    </div>
    </AppInfoContext.Provider>
}