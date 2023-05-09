import {Button, Dialog, DialogBody, DialogFooter} from "@blueprintjs/core";
import React from "react";

export interface ConfirmDialogProps {
    isOpen?: boolean
    onClose?: () => void
    onConfirm?: () => void
}
export function ConfirmDeleteDialog(props: ConfirmDialogProps){

    return <Dialog onClose={props.onClose} isOpen={props.isOpen} title="Confirm deleting" className="bp4-dark">
        <DialogBody>
            <div className="bp4-text-large">
                <p>Shared files will be deleted permanently. Action is not reversible.</p>
            </div>
        </DialogBody>
        <DialogFooter actions={[<Button intent="danger" text="Delete" onClick={props.onConfirm} />, <Button text="Maybe later" onClick={props.onClose} />]} />
    </Dialog>
}