package com.eip.roucou_c.spred;


/**
 * Created by cleme_000 on 04/03/2016.
 */
public final class UserEntity {

//    private static volatile UserEntity instance = null;
//
//    public ProfileEntity _profileEntity = null;
//    public TokenEntity _tokenEntity = null;
//    public SettingsEntity _settingsEntity = null;
//    public List<RoomEntity> _roomEntityList = null;
//    private Manager _manager;
//    public OrganizationEntity _organizationEntity = null;
//    public RoomEntity _roomEntity = null;
//
//    private UserEntity(Manager _manager) {
//        this._manager = _manager;
//        this.refresh();
//    }
//
//    public final static UserEntity getInstance(Manager manager) {
//        if (UserEntity.instance == null) {
//            synchronized(UserEntity.class) {
//                if (UserEntity.instance == null) {
//                    UserEntity.instance = new UserEntity(manager);
//                }
//            }
//        }
//        return UserEntity.instance;
//    }
//
//    public void refresh(){
//        if (refreshToken()) {
//            if (_tokenEntity != null) {
//                String profile_public_id = _tokenEntity.get_profile_public_id();
//                if (profile_public_id != null) {
//                    _profileEntity = _manager._profileManager.selectByPublic_id(profile_public_id);
//                    _settingsEntity = _manager._settingsManager.selectByProfilePublicId(profile_public_id);
//                    _roomEntityList = _manager._roomsManager.selectAllByProfile_id(_profileEntity.get_public_id());
//                    String organization_public_id_selected = _manager._globalManager.select("organization_public_id_selected");
//
//                    if (organization_public_id_selected != null) {
//                        _organizationEntity = _manager._organizationManager.selectByPublic_id(organization_public_id_selected);
//                    }
//                    else {
//                        _organizationEntity = null;
//                    }
//
//                    String room_public_id_selected = _manager._globalManager.select("room_public_id_selected");
//                    if (room_public_id_selected != null) {
//                        _roomEntity = _manager._roomsManager.selectByPublic_id(room_public_id_selected);
//                    }
//                    else {
//                        _roomEntity = null;
//                    }
//                }
//            }
//        }
//    }
//
//    public void addProfile(ProfileEntity profileEntity) {
//        _manager._profileManager.add(profileEntity);
//
//        this._profileEntity = profileEntity;
//
//        this.refresh();
//
//        this.initAddProfile();
//
//    }
//
//    private void initAddProfile() {
//        if (_settingsEntity == null) {
//            SettingsEntity settingsEntity = new SettingsEntity();
//            settingsEntity.set_profile_public_id(_profileEntity.get_public_id());
//            _manager._settingsManager.add(settingsEntity);
//            _settingsEntity = settingsEntity;
//        }
//    }
//
//    public void set_tokenEntity(TokenEntity tokenEntity) {
//        this._tokenEntity = tokenEntity;
//        this.update_tokenEntity();
//    }
//
//    public void update_tokenEntity() {
//        this._manager._tokenManager.modify(_tokenEntity);
//    }
//
//    public void set_profileEntity(ProfileEntity profileEntity) {
//        this._profileEntity = profileEntity;
//        this.update_profileEntity();
//    }
//
//    public void update_profileEntity() {
//        this._manager._profileManager.modifyProfil(_profileEntity);
//    }
//
//    public boolean refreshToken() {
//        String profile_public_id_connected = _manager._globalManager.select("profile_public_id_connected");
//
//        if (profile_public_id_connected != null) {
//            _tokenEntity = _manager._tokenManager.selectByProfilePublicId(profile_public_id_connected);
//            if (_tokenEntity != null) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void logout() {
//        if (_tokenEntity != null) {
//            _manager._tokenManager.deleteByProfile_id(_tokenEntity.get_profile_public_id());
//        }
//        _profileEntity = null;
//        _tokenEntity = null;
//        _settingsEntity = null;
//        _roomEntityList = null;
//        _organizationEntity = null;
//        _roomEntity = null;
//
//        _manager._globalManager.deleteGlobal("profile_public_id_connected");
//        _manager._globalManager.deleteGlobal("organization_public_id_selected");
//        _manager._globalManager.deleteGlobal("room_public_id_connected");
//    }
//
//    public void addTokenEntity(TokenEntity tokenEntity) {
//        if (tokenEntity != null) {
//            _manager._tokenManager.add(tokenEntity);
//            _manager._globalManager.addGlobal("profile_public_id_connected", tokenEntity.get_profile_public_id());
//            this._tokenEntity = tokenEntity;
//        }
//    }
//
//    public void update_settingsEntity() {
//        this._manager._settingsManager.modify(_settingsEntity);
//    }
//
//    public void refreshRoomEntityList() {
//        if (_profileEntity != null) {
//            _roomEntityList = _manager._roomsManager.selectAllByProfile_id(_profileEntity.get_public_id());
//        }
//    }
}
