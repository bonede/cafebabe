import React from 'react';
import './App.css';
import {PanelNode, PanelRoot} from "./comp/panel/root";
import {Panel} from "./comp/panel/panel";


function App() {
    let header = <div>JavaExplorer</div>
    let footer = <div>&copy; 2022</div>
    let leftNode: PanelNode = {
        width: 1,
        children: <Panel title="left" />
    }
    let centerNode: PanelNode = {
        width: 1,
        children: <Panel title="center" />
    }
    let rightNode: PanelNode = {
        width: 1,
        children: <Panel title="right" />
    }
    let rootNode: PanelNode = {
        orientation: "horizontal",
        children: [
            leftNode,
            centerNode,
            rightNode
        ]
    }
    return (
        <PanelRoot child={rootNode} header={header} footer={footer} />
    );
}

export default App;
