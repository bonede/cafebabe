import './App.scss'
import React, {createContext, useEffect, useState} from "react";
import {
    ApiClient,
    AppInfo,
    ClassFile,
    CompileResult,
    CompilerOps,
    OutputWindowState,
    ShareResp,
    SrcFile
} from "../../api/ApiClient";
import {ClassFileWindow} from "../classFileWindow/ClassFileWindow";
import {OutputMsg, OutputType, OutputWindow} from "../outputWindow/OutputWindow";
import {EditorWindow} from "../editorWindow/EditorWindow";
import {Panel, PanelGroup, PanelResizeHandle} from "react-resizable-panels";
import {ShareRespDialog} from "../dialog/ShareRespDialog";
import {DeleteShareDialog} from "../dialog/DeleteShareDialog";
import {getDeletingToken, getShareId} from "../Utils";
import {ConfirmDeleteDialog} from "../dialog/ConfirmDeleteDialog";
import {AboutDialog} from "../dialog/AboutDialog";
import {PrivacyPolicyDialog} from "../dialog/PrivacyPolicyDialog";
import {CookiePolicyDialog} from "../dialog/CookiePolicyDialog";

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
    } as OutputWindowState)
    const [shareId, setShareId] = useState(null as string | null)
    const [deletingToken, setDeletingToken] = useState(null as string | null)
    const [aboutOpen, setAboutOpen] = useState(false)
    const [cookieOpen, setCookieOpen] = useState(false)
    const [privacyOpen, setPrivacyOpen] = useState(false)
    useEffect(() => {
        apiClient.getAppInfo().then(a => setAppInfo(a))
        const outputState = apiClient.getOutputWindowState()
        if(outputState){
            setOutputState(outputState)
        }
        setShareId(getShareId(window.location.href))
        setDeletingToken(getDeletingToken(window.location.href))
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

    const handleDeleteShare = async () => {
        try {
            await apiClient.deleteShare(location.href, deletingToken!)
            const u = new URL(location.href)
            u.searchParams.delete("d")
            u.searchParams.delete("s")
            history.replaceState(null, '', u.href);
            pushMsg('stdout', 'Files deleted')
            setShareId(null)
            setDeletingToken(null)
        } catch (e) {
            pushMsg("stderr", e + "")
            setShareId(null)
            setDeletingToken(null)
        }
    }

    const handleCancelDelete = () => {
        setShareId(null)
        setDeletingToken(null)
    }

    const handleClearCache = () => {
        apiClient.clearCache()
        pushMsg("stdout", "Local cache cleared")
    }

    return <AppInfoContext.Provider value={appInfo}>
     <div id="app" className="bp4-dark">
         <AboutDialog onClose={() => setAboutOpen(!aboutOpen)} isOpen={aboutOpen} />
         <PrivacyPolicyDialog onClose={() => setPrivacyOpen(!privacyOpen)} isOpen={privacyOpen} />
         <CookiePolicyDialog onClose={() => setCookieOpen(!cookieOpen)} isOpen={cookieOpen} />
         <ConfirmDeleteDialog onClose={handleCancelDelete} onConfirm={handleDeleteShare} isOpen={shareId != null && deletingToken != null} />
         <ShareRespDialog onClose={() => setShareResp(undefined)} shareResp={shareResp} />
         <DeleteShareDialog isOpen={deleting} onClose={() => setDeleting(false)} onDeleted={() => setDeleting(false)} />
         <PanelGroup autoSaveId="cafebabe" direction="horizontal">
             <Panel defaultSize={45}>
                 <PanelGroup direction="vertical">
                     <Panel>
                         <EditorWindow
                             onAboutClick={() => setAboutOpen(true)}
                             onCookiePolicyClick={() => setCookieOpen(true)}
                             onPrivacyPolicyClick={() => setPrivacyOpen(true)}
                             onClearCacheClick={handleClearCache}
                             onDeleteReq={handleDeleteReq}
                             onShareReq={handleShareReq}
                             selectLine={classFileLine}
                             onSelectLines={lines => setSelectedLines(lines)}
                             onCompile={handleCompile}
                             compilers={appInfo?.compilers || []}
                         />
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