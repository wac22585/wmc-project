<template>
    <v-form ref="form" @submit-prevent="save" @submit="save">
        <label for="">Firstname</label>
        <v-text-field :rules="[rules.required, rules.counter]" v-model="user.firstname" append-inner-icon="mdi-pencil"  variant="underlined"></v-text-field>
        <label for="">Lastname</label>
        <v-text-field :rules="[rules.required, rules.counter]" v-model="user.lastname" append-inner-icon="mdi-pencil"  variant="underlined"></v-text-field>

        <label for="">Email address</label>
        <v-text-field :rules="[rules.required, rules.email]" append-inner-icon="mdi-pencil" v-model="user.email" variant="underlined"></v-text-field>

        <label for="">Phone-Number</label>
        <v-text-field :rules="[rules.required, rules.phoneNumber]" append-inner-icon="mdi-pencil" v-model="user.phoneNumber" variant="underlined"></v-text-field>

        <label for="" v-if="!isAccount">Roles</label>
        <v-select
            :rules="[rules.required, rules.roles]"
            :items="roles"
            v-model="selectedRoles"
            bg-color="none"
            variant="underlined"
            multiple
            v-if="!isAccount"
        >
            <template v-slot:selection="{item, index}">
                <v-chip v-if="index < 2">
                    <span>{{ item.title }}</span>
                </v-chip>
                <span 
                v-if="index === 2"
                class="text-grey text-caption align-self-center"
                >
                    (+{{ selectedRoles.length - 2 }} others)
                </span>
            </template>
        </v-select>
        <DatePicker :defaultDate="computedDefaultDate" :border="true"/>

        <div class="btn-div">
            <Button v-if="!isAccount" :useWhiteBackground="true" label="Delete"  @click="deleteUser"/>
            <Button label="Save" @click="save"/>
        </div>
    </v-form>
</template>

<script setup>
    import axios from 'axios';
    import DatePicker from '@/components/DatePicker.vue';
    import Button from '@/components/Button.vue';
</script>

<script>   
    export default {
        data() {
            return {
                roles: [],
                user: {},
                id: this.$route.params.id || null,
                selectedRoles: [],
                rules: {
                    required: value => !!value || 'Required.',
                    counter: value => value.length <= 20 || 'Max 20 characters.',
                    email: value => {
                        const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
                        return pattern.test(value) || 'Invalid e-mail.'
                    },
                    phoneNumber: value => {
                        const pattern = /^\d{7,15}$/;
                        return pattern.test(value) || 'Invalid phone number.';
                    },
                    roles: value => this.selectedRoles.length > 0 || 'At least one role must be selected.',
                },
                isAccount: false,
            }
        },
        computed: {
            computedDefaultDate() {
                let date = new Date(this.user.birthdate);
                return new Date(date.getFullYear(), date.getMonth(), date.getDate())
            }
        },
        async mounted() {
            try {
                if(this.id == null) {
                    this.id = localStorage.getItem('id');
                    this.isAccount = true;
                }
                const response = await axios.get(`users/get/${this.id}`);
                if(response.status == 200) {
                    this.user = response.data;
                }
                else {
                   alert(response.data)
                }
               
                const roles = await axios.get('roles/all');
                this.roles = roles.data;
                this.roles = this.roles.map(role => role.name);
                this.selectedRoles = this.user.roles.map(userRoleName => {
                    let matchingRole = this.roles.find(role => role === userRoleName);
                    return matchingRole;
                }).filter(id => id !== null);
            } catch(error) {
                alert(error);
            }
        },
        methods: {
            validateField(fieldValue, rules) {
                for (let rule of rules) {
                    const result = rule(fieldValue);
                    if (result !== true) {
                        return result;
                    }
                }
                return true;
            },
            async deleteUser() {
                try {
                    await axios.put(`users/delete/${this.id}`);
                    this.$router.push({ name: 'Home' });
                } catch (error) {
                    alert(error.response.data);
                }
            },
            async save() {
                const user = this.user;
                const rules = this.rules;
                if (this.validateField(user.firstname, [rules.required, rules.counter]) !== true ||
                this.validateField(user.lastname, [rules.required, rules.counter]) !== true ||
                this.validateField(user.email, [rules.required, rules.email]) !== true ||
                this.validateField(user.phoneNumber, [rules.required, rules.phoneNumber]) !== true) {
                return;
                }
                if(!this.isAccount && this.validateField(this.selectedRoles, [rules.required, rules.roles]) !== true) return;

                try {  
                    console.log(this.user.roles, this.selectedRoles)
                    this.user.roles = this.selectedRoles;
                    const response = await axios.put(`users/update/${this.id}`, this.user);
                    if (response.status === 200) {
                        this.$router.push({ name: 'Home' });
                    } else {
                       alert(response.data)
                    }
                } catch (error) {
                  alert(error)
                }
            }
        }
    }
</script>

<style>
    .btn-div {
        display: flex;
        justify-content: end;
    }
</style>