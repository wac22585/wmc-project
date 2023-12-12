<template>
    <v-container>
        <button @click="back">
            <v-icon size="40">mdi-arrow-left</v-icon>
        </button>
        <div class="title">New User</div>
        <div class="subtitle">Create a new User</div>
        <v-form @submit-prevent="addUser">
            <v-row>
                <v-col>
                    <label for="">Firstname</label>
                    <InputField 
                    :model-value="user.firstname" 
                    @update:model-value="newValue => user.firstname = newValue" inputType="text" />
                </v-col>
                <v-col>
                    <label for="">Lastname</label>
                    <InputField 
                    :model-value="user.lastname" 
                    @update:model-value="newValue => user.lastname = newValue" inputType="text" />
                </v-col>
            </v-row>

            <v-row>
                <v-col>
                    <label for="">E-Mail-Address</label>
                    <InputField 
                    :model-value="user.email" 
                    @update:model-value="newValue => user.email = newValue" inputType="text" />
                </v-col>
                <v-col>
                    <label for="">Phone-Number</label>
                    <InputField></InputField>
                </v-col>
            </v-row>

            <v-row>
                <v-col>
                    <label for="">Roles</label>
                    <Select :roles="roles" v-model="selectedRoles" />
                </v-col>
                <v-col>
                    <label for="">Date of Birth</label>
                    <InputField></InputField>
                </v-col>
            </v-row>

            <v-row>
                <v-col>
                    <label for="">Password</label>
                    <InputField 
                        width="400px"
                        :model-value="user.password" 
                        @update:model-value="newValue => user.password = newValue" />
                </v-col>
                <v-col>
                    <label for="">Confirm Password</label>
                    <InputField 
                        :model-value="confirmpassword" 
                        @update:model-value="newValue => confirmpassword = newValue" />
                </v-col>
            </v-row>

            <v-row>
                <v-col>
                    <div class="btn-div">
                        <Btn @click="addUser" label="Create" />
                    </div>
                </v-col>
            </v-row>
        </v-form>
    </v-container>
</template>
  
<script>
    import InputField from '@/components/Input.vue';
    import Btn from '@/components/Button.vue';
    import Select from '@/components/Select.vue';
    import axios from 'axios';

    export default {
        data() {
            return {
                user: {},
                confirmpassword: '',
                roles: [],
                selectedRoles: [],
            }
        },
        components: {
            InputField,
            Btn,
            Select,
        },
        async mounted() {
            try {
                const response = await axios.get('roles/all');
                for(const r of response.data) 
                {
                    const name = r.name.charAt(0) + r.name.slice(1).toLowerCase()
                    name.replace("_", " ");
                    this.roles.push(name);
                }
            } catch (error) {
                console.log('An error occurred: ', error)
            }
        },
        methods: {
            back() {
                this.$router.push({ name: 'Home' });
            },
            async addUser() {
                try {
                    let user = this.user;

                    user.firstname = user.firstname.charAt(0).toUpperCase() + user.firstname.slice(1);
                    user.lastname = user.lastname.charAt(0).toUpperCase() + user.lastname.slice(1);
                    user.phonenumber = null;
                    user.created = new Date().toISOString().slice(0, 10);

                    const response = await axios.post('users/add', user);

                    if (response.status === 201) {
                        console.log('User added successfully');
                    } else {
                    console.error('Error adding user');
                    }
                } catch (error) {
                console.error('An error occurred:', error);
                }
            }
        }
    };
</script>

<style scoped>
    .title {
        margin-top: 10px;
        color: black;
        font-size: 30px;
    }

    .subtitle {
        color: black;
        font-size: 20px;
        margin-top: -10px;
        margin-bottom: 40px;
    }

    .col-end {
        margin-left: auto;
    }

    .btn-div {
        display: flex;
        justify-content: flex-start;
    }
</style>
  