import './editor.css'
import * as monaco from 'monaco-editor';
import {useEffect, useRef} from "react";

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
}
export function Editor(props: EditorProps){

    const editorElement = useRef(null);
    useEffect(() => {
        monaco.editor.create(editorElement.current!, {
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
            }
        })
    }, []);

    return <div style={{width: "100%"}} ref={editorElement} className="code-editor"></div>
}