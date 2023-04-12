import './app.css'
import React, {useEffect, useState} from "react";
import {ApiClient, AppInfo, ClassFile, CompileResult} from "../../api/ApiClient";


import {Mosaic, MosaicPath} from 'react-mosaic-component';
import {ClassFileWindow} from "../classFileWindow/classFileWindow";
import {OutputMsg, OutputWindow} from "../outputWindow/outputWindow";
import {EditorWindow} from "../editorWindow/editorWindow";


type WindowType = 'editor' | 'output' | 'classFile'



export const JavaExplorerApp = () => {
    const apiClient = ApiClient.getClient()
    const [selectedFile, setSelectedFile] = useState(undefined as string | undefined)
    const [selectedLines, setSelectedLines] = useState([] as number[])
    const [outputMsgs, setOutputMsg] = useState([] as OutputMsg[])
    const [classFiles, setClassFiles] = useState([] as ClassFile[])
    const [appInfo, setAppInfo] = useState(null as AppInfo | null)

    useEffect(() => {
        apiClient.getAppInfo().then(a => setAppInfo(a))
    }, [])

    const handleCompile = (result: CompileResult) => {
        if(result.classFiles != null){
            setClassFiles(result.classFiles)
        }
        if(result.stdout){
            outputMsgs.push()
            setOutputMsg([...outputMsgs, {
                type: "stdout",
                msg: result.stdout
            }])
        }
        if(result.stderr){
            setOutputMsg([...outputMsgs, {
                type: "stderr",
                msg: result.stderr
            }])
        }
    }
    const handleClearMsg = () => {
        setOutputMsg([])
    }
    const windowForId = (id: WindowType, path: MosaicPath) => {
        switch (id){
            case "editor": return <EditorWindow onSelectLines={(lines) => setSelectedLines(lines) } onCompile={handleCompile} mosaicPath={path} compilers={appInfo!.compilers} />
            case "output": return <OutputWindow onClearMsg={handleClearMsg} mosaicPath={path} outputMsgs={outputMsgs}  />
            case "classFile": return <ClassFileWindow selectedFile={selectedFile} selectedLines={selectedLines} mosaicPath={path} classFiles={classFiles}  />
        }
    }
    return <div id="app">
        {
            appInfo ? <Mosaic<WindowType>
                className="mosaic-blueprint-theme bp4-dark"
                blueprintNamespace="bp4"
                renderTile={windowForId}
                initialValue={{
                    direction: 'row',
                    first: {
                        direction: 'column',
                        first: 'editor',
                        second: 'output',
                        splitPercentage: 60,
                    },
                    second: 'classFile',
                    splitPercentage: 50,
                }}
            /> : null
        }
    </div>
}