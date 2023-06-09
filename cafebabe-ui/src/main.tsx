import './index.css'
import {JavaExplorerApp} from "./comp/app/App"

import * as ReactDOM from 'react-dom/client'
import "normalize.css"
import '@blueprintjs/core/lib/css/blueprint.css'
import '@blueprintjs/select/lib/css/blueprint-select.css'
import '@blueprintjs/popover2/lib/css/blueprint-popover2.css'
import '@blueprintjs/icons/lib/css/blueprint-icons.css'

ReactDOM.createRoot(document.getElementById('root') as HTMLDivElement).render(
    <JavaExplorerApp />,
)