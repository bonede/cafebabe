import {Button, Dialog, DialogBody, DialogFooter} from "@blueprintjs/core";
import React from "react";

export interface CookieDialogProps {
    isOpen?: boolean
    onClose: () => void
}
export function CookiePolicyDialog(props: CookieDialogProps){

    return <Dialog onClose={props.onClose} isOpen={props.isOpen} title="About Cafebabe" className="bp4-dark">
        <DialogBody>
            <div className="bp4-text-large">
                <h3>We use cookies and local storage to</h3>
                <ul>
                    <li>Store user preference</li>
                    <li>Source code</li>
                </ul>

            </div>
        </DialogBody>
        <DialogFooter actions={[<Button text="Close" onClick={props.onClose} />]} />
    </Dialog>
}