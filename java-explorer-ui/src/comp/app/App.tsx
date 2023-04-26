import './App.css'
import React, {createContext, useEffect, useState} from "react";
import {
    ApiClient,
    AppInfo,
    ClassFile,
    CompileResult,
    CompilerOps,
    OutputState,
    ShareResp,
    SrcFile
} from "../../api/ApiClient";
import {ClassFileWindow} from "../classFileWindow/ClassFileWindow";
import {OutputMsg, OutputType, OutputWindow} from "../outputWindow/OutputWindow";
import {EditorWindow} from "../editorWindow/EditorWindow";
import {Panel, PanelGroup, PanelResizeHandle} from "react-resizable-panels";
import {ShareRespDialog} from "../dialog/ShareRespDialog";
import {DeleteShareDialog} from "../dialog/DeleteShareDialog";

export const AppInfoContext = createContext(undefined as AppInfo | undefined)

export const JavaExplorerApp = () => {
    const apiClient = ApiClient.getClient()
    const [selectedFile, setSelectedFile] = useState(undefined as string | undefined)
    const [selectedLines, setSelectedLines] = useState([] as number[])
    const [outputMsgs, setOutputMsg] = useState([] as OutputMsg[])
    const [classFiles, setClassFiles] = useState([] as ClassFile[])
    const [appInfo, setAppInfo] = useState(undefined as AppInfo | undefined)
    const [shareResp, setShareResp] = useState(undefined as ShareResp | undefined)
    const [deleting, setDeleting] = useState(false)
    const [classFileName, setClassFileName] = useState(undefined as string | undefined)
    const [classFileLine, setClassFileLine] = useState(undefined as number | undefined)
    const [outputState, setOutputState] = useState({
        pinMsg: true
    } as OutputState)

    useEffect(() => {
        apiClient.getAppInfo().then(a => setAppInfo(a))
        const outputState = apiClient.getOutputState()
        if(outputState){
            setOutputState(outputState)
        }
    }, [])


    const pushMsg = (type: OutputType, msg: string) => {
        const outputMsg = {
            type,
            msg
        }
        if(outputState.pinMsg){
            setOutputMsg([...outputMsgs, outputMsg])
        }else {
            setOutputMsg([outputMsg])
        }

    }

    const clearMsgs = () => {
        if(!outputState.pinMsg){
            setOutputMsg([])
        }
    }

    const handlePinMsgClick  = () => {
        outputState.pinMsg = !outputState.pinMsg
        setOutputState({
            ...outputState
        })
        apiClient.saveOutputState(outputState)
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
        if(!result.stdout && !result.stderr){
            clearMsgs()
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

    const handleDeleteReq = () => {
        setDeleting(true)
    }

    return <AppInfoContext.Provider value={appInfo}>
     <div id="app" className="bp4-dark">
         <ShareRespDialog onClose={() => setShareResp(undefined)} shareResp={shareResp} />
         <DeleteShareDialog isOpen={deleting} onClose={() => setDeleting(false)} onDeleted={() => setDeleting(false)} />
         <PanelGroup autoSaveId="cafebabe" direction="horizontal">
             <Panel defaultSize={45}>
                 <PanelGroup direction="vertical">
                     <Panel>
                         <EditorWindow onDeleteReq={handleDeleteReq} onShareReq={handleShareReq} selectLine={classFileLine} onSelectLines={lines => setSelectedLines(lines)} onCompile={handleCompile} compilers={appInfo?.compilers || []} />
                     </Panel>
                     <PanelResizeHandle><div style={{background: "#00000000", height: 5}}></div></PanelResizeHandle>
                     <Panel  defaultSize={30}>
                         <OutputWindow pinMsgs={outputState.pinMsg} onPinMsgsClick={handlePinMsgClick} outputMsgs={outputMsgs} onClearMsg={handleClearMsg} />
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