import './editor.css'
import * as monaco from 'monaco-editor';
import {editor} from 'monaco-editor';
import {useEffect, useRef, useState} from "react";
import IStandaloneCodeEditor = editor.IStandaloneCodeEditor;

monaco.editor.defineTheme('vs-dark-enhanced', {
    base: 'vs-dark',
    inherit: true,
    rules: [],
    colors: {
        'editor.foreground': "#bdc1c6",
        'editor.lineHighlightBackground': '#303134',
        'editor.lineHighlightBorder': '#303134',
        "editor.overviewRulerBorder": "false"
    }
});

export interface EditorProps{
    lang: string
    content: string
    onContentChange?: (content: string) => void
}
export function Editor(props: EditorProps){
    let [editor, setEditor] = useState<IStandaloneCodeEditor>()
    const editorElement = useRef(null)
    useEffect(() => {
        editor = monaco.editor.create(editorElement.current!, {
            theme: "vs-dark-enhanced",
            value: props.content,
            language: props.lang,
            fontSize: 18,
            automaticLayout: true,
            minimap: {enabled: false},
            smoothScrolling: true,
            scrollbar: {
                vertical: 'visible',
                horizontal: 'visible',
                useShadows: false
            },
        })
        editor.getModel()?.onDidChangeContent(e => {
            props.onContentChange?.(editor?.getValue() || "")
        })
        setEditor(editor)
    }, []);
    const handleContentChange = () => {
        if(props.content && editor){
            editor.setValue(props.content)
            props.onContentChange?.(props.content)
        }
    }
    useEffect(handleContentChange, [props.content])
    return <div style={{width: "100%", height: "100%"}} ref={editorElement} className="code-editor"></div>
}