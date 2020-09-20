import React from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import GitHubIcon from '@material-ui/icons/GitHub';
import IconButton from "@material-ui/core/IconButton";
import {Typography} from "@material-ui/core";
import Container from "@material-ui/core/Container";
import FormSubmitButton from "./FormSubmitButton";


export default function AddOrganizationDialog() {
    const [open, setOpen] = React.useState(false);
    const [organization, setOrganization] = React.useState();
    const [currentRepository, setCurrentRepository] = React.useState();

    const handleClickOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const handleSubmit = () => setOpen(false);
    const handleChange = (e) => {
        e.preventDefault();
        setOrganization(e.target.value);
    };

    const handleLoad = (repository, description) => setCurrentRepository({url: repository, description: description});

    return (
        <div>
            <IconButton onClick={handleClickOpen}>
                <GitHubIcon />
            </IconButton>
            <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">Github organization</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        To add all the organization's public repositories from Github, please, enter the organization name.
                        <br/>
                        Name is case sensitive.
                        <br/>
                        It may take a while to get all the repositories, please do not cancel the download.
                    </DialogContentText>
                    <TextField
                        autoComplete="off"
                        autoFocus
                        margin="dense"
                        id="name"
                        placeholder="JetBrains-Research"
                        label="Organization name"
                        fullWidth
                        onChange={handleChange}
                    />

                    {currentRepository && (
                        <Container maxWidth="sm">
                            <Typography>
                                {currentRepository}
                            </Typography>
                        </Container>
                    )}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} color="primary">
                        Cancel
                    </Button>
                    <FormSubmitButton onClick={handleSubmit} org={organization} onLoad={handleLoad} />
                </DialogActions>
            </Dialog>
        </div>
    );
}