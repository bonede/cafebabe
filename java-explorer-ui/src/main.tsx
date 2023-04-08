import './index.css'
import {JavaExplorerApp} from "./comp/app/app";

import * as ReactDOM from 'react-dom/client';
import "normalize.css";
import 'react-mosaic-component/react-mosaic-component.css';
import '@blueprintjs/core/lib/css/blueprint.css';
import '@blueprintjs/icons/lib/css/blueprint-icons.css';

ReactDOM.createRoot(document.getElementById('root') as HTMLDivElement).render(
    <JavaExplorerApp />,
)